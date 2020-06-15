package com.schemaexplorer.view;

import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeListener;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.EventDispatcher;
import com.schemaexplorer.model.FieldData;
import com.schemaexplorer.model.SObjectData;
import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.util.SOQLQueryBuilder;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.util.*;

public class SchemaExplorerWindow {

    private JPanel panel;
    private CheckboxTree sObjectsTree;
    private JTabbedPane queriesTabs;

    private final EventDispatcher<ConnectionLoadListener> connectionLoadDispatcher =
            EventDispatcher.create(ConnectionLoadListener.class);
    private final EventDispatcher<SObjectLoadListener> sObjectLoadDispatcher =
            EventDispatcher.create(SObjectLoadListener.class);

    private Map<SalesforceConnection, Map<String, SObjectData>> selectedFieldsByConnection = new HashMap<>();

    public JPanel getContent() {
        return panel;
    }

    public void loadSalesforceConnections(@NotNull List<SalesforceConnection> salesforceConnections) {
        getRootNode().removeAllChildren();
        List<SalesforceConnection> connections = new ArrayList<>(salesforceConnections);
        connections.sort(Comparator.comparing(SalesforceConnection::getName));
        connections.forEach(connection -> addConnection(connection));
        refresh();
    }

    public void addConnectionLoadListener(@NotNull ConnectionLoadListener listener) {
        this.connectionLoadDispatcher.addListener(listener);
    }

    public void addSObjectLoadListener(@NotNull SObjectLoadListener listener) {
        this.sObjectLoadDispatcher.addListener(listener);
    }

    private void addConnection(SalesforceConnection connection) {
        connection = SerializationUtils.clone(connection);
        DefaultMutableTreeNode connectionNode = new DefaultMutableTreeNode(connection);
        getRootNode().add(connectionNode);

        loadSObjects(connection, connectionNode);
    }

    private void loadSObjects(SalesforceConnection connection, DefaultMutableTreeNode connectionNode) {
        connectionNode.removeAllChildren();
        connection.getSortedSObjectDataSet().forEach(sObjectData -> {
            CheckedTreeNode sObjectNode = createNewCheckedTreeNode(connectionNode, sObjectData);
            loadFields(sObjectData, sObjectNode);
        });
        if(!connection.hasObjects()) {
            addLoadingNode(connectionNode);
        }
    }

    private void loadFields(SObjectData sObjectData, CheckedTreeNode sObjectNode) {
        sObjectNode.removeAllChildren();
        if(sObjectData.hasFields()) {
            sObjectData.getSortedFields().forEach(fieldData -> createNewCheckedTreeNode(sObjectNode, fieldData));
            sObjectsTree.expandPath(new TreePath(sObjectNode.getPath()));
        } else {
            addLoadingNode(sObjectNode);
        }
    }

    private void addLoadingNode(DefaultMutableTreeNode parentNode) {
        parentNode.add(new DefaultMutableTreeNode("Loading..."));
    }

    private void refresh() {
        ((DefaultTreeModel) sObjectsTree.getModel()).reload();
        sObjectsTree.treeDidChange();
        sObjectsTree.expandPath(new TreePath(getRootNode()));
    }

    private DefaultMutableTreeNode getRootNode() {
        DefaultTreeModel model = (DefaultTreeModel) sObjectsTree.getModel();
        return (DefaultMutableTreeNode) model.getRoot();
    }

    private CheckedTreeNode createNewCheckedTreeNode(DefaultMutableTreeNode parentNode, Object userData) {
        CheckedTreeNode node = new CheckedTreeNode(userData);
        node.setChecked(false);
        node.setEnabled(true);
        parentNode.add(node);
        return node;
    }

    private void createUIComponents() {
        this.sObjectsTree = new CheckboxTree(new SchemaExplorerTreeCellRenderer(), null);
        this.sObjectsTree.addCheckboxTreeListener(createCheckboxTreeListener());
        this.sObjectsTree.addTreeWillExpandListener(createTreeWillExpandListener());
        DefaultTreeModel model = (DefaultTreeModel) this.sObjectsTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        rootNode.setUserObject("Salesforce Schema Explorer");
    }

    private JTextArea getQueryTextArea(SalesforceConnection connection) {
        int tabIndex = queriesTabs.indexOfTab(connection.getName());
        JBScrollPane queryPane = null;
        if(tabIndex < 0) {
            queryPane = new JBScrollPane(new JTextArea());
            queriesTabs.addTab(connection.getName(), queryPane);
        } else {
            queryPane = (JBScrollPane) queriesTabs.getComponentAt(tabIndex);
        }

        return (JTextArea) queryPane.getViewport().getView();
    }

    private void removeTab(SalesforceConnection connection) {
        int tabIndex = queriesTabs.indexOfTab(connection.getName());
        if(tabIndex >= 0) {
            queriesTabs.remove(tabIndex);
        }
    }

    private void selectTab(SalesforceConnection connection) {
        int tabIndex = queriesTabs.indexOfTab(connection.getName());
        if(tabIndex >= 0) {
            queriesTabs.setSelectedIndex(tabIndex);
        }
    }

    private TreeWillExpandListener createTreeWillExpandListener() {
        return new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeExpansionEvent
                        .getPath().getLastPathComponent();

                if (node.getUserObject() instanceof SalesforceConnection) {
                    SalesforceConnection connection = (SalesforceConnection) node.getUserObject();
                    if(!connection.hasObjects()) {
                        connectionLoadDispatcher.getMulticaster().onConnectionLoad(connection);
                        loadSObjects(connection, node);
                    }

                } else if (node.getUserObject() instanceof SObjectData) {
                    SObjectData sObjectData = (SObjectData) node.getUserObject();
                    if(!sObjectData.hasFields()) {
                        triggerOnSObjectLoadEvent(node);
                    }
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
                // Do nothing.
            }
        };
    }

    private void triggerOnSObjectLoadEvent(DefaultMutableTreeNode sObjectNode) {
        SObjectData sObjectData = (SObjectData) sObjectNode.getUserObject();
        sObjectLoadDispatcher.getMulticaster().onSObjectLoad(sObjectData);
        if(sObjectData.hasFields()) {
            loadFields(sObjectData, (CheckedTreeNode) sObjectNode);
        }
    }

    private CheckboxTreeListener createCheckboxTreeListener() {
        return new CheckboxTreeListener() {

            private boolean isBatchFieldSelectionRunning = false;

            @Override
            public void nodeStateChanged(@NotNull CheckedTreeNode node) {
                if (node.getUserObject() instanceof FieldData) {
                    updateSelectedField(node);
                } else if (node.getUserObject() instanceof SObjectData) {
                    updateSelectedSObject(node);
                }
            }

            private void updateSelectedSObject(CheckedTreeNode node) {
                SObjectData sObjectData = (SObjectData) node.getUserObject();

                // Calls the loading event.
                if(!sObjectData.hasFields()) {
                    triggerOnSObjectLoadEvent(node);
                }

                // Gets nodes data.
                DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) node.getParent();
                SalesforceConnection connection = (SalesforceConnection) connectionNode.getUserObject();

                // Select / unselect children fields.
                isBatchFieldSelectionRunning = true;
                for(int index = 0; index < node.getChildCount(); index++) {
                    CheckedTreeNode fieldNode = (CheckedTreeNode) node.getChildAt(index);
                    if (node.isChecked()) {
                        //fieldNode.setChecked(true);
                        selectFieldNode(fieldNode);
                    } else {
                        //fieldNode.setChecked(false);
                        unselectFieldNode(fieldNode);
                    }
                }
                isBatchFieldSelectionRunning = false;

                updateSOQLText(connection);
            }

            private void updateSelectedField(CheckedTreeNode node) {
                if(isBatchFieldSelectionRunning) {
                    return;
                }
                if (node.isChecked()) {
                    selectFieldNode(node);
                } else {
                    unselectFieldNode(node);
                }
                DefaultMutableTreeNode sObjectNode = (DefaultMutableTreeNode) node.getParent();
                DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) sObjectNode.getParent();
                updateSOQLText((SalesforceConnection) connectionNode.getUserObject());
            }

            private void selectFieldNode(CheckedTreeNode fieldNode) {
                CheckedTreeNode sObjectNode = (CheckedTreeNode) fieldNode.getParent();
                DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) sObjectNode.getParent();

                SalesforceConnection connection = (SalesforceConnection) connectionNode.getUserObject();
                SObjectData sObjectData = (SObjectData) sObjectNode.getUserObject();
                FieldData fieldData = (FieldData) fieldNode.getUserObject();

                if (!selectedFieldsByConnection.containsKey(connection)) {
                    selectedFieldsByConnection.put(connection, new HashMap<>());
                }
                Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
                if (!sObjectDataMap.containsKey(sObjectData.getName())) {
                    sObjectDataMap.put(sObjectData.getName(), new SObjectData(sObjectData));
                }
                sObjectDataMap.get(sObjectData.getName()).addField(fieldData);
            }

            private void unselectFieldNode(CheckedTreeNode fieldNode) {
                CheckedTreeNode sObjectNode = (CheckedTreeNode) fieldNode.getParent();
                DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) sObjectNode.getParent();

                SalesforceConnection connection = (SalesforceConnection) connectionNode.getUserObject();
                SObjectData sObjectData = (SObjectData) sObjectNode.getUserObject();
                FieldData fieldData = (FieldData) fieldNode.getUserObject();

                Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
                if (!sObjectDataMap.containsKey(sObjectData.getName())) {
                    return;
                }
                sObjectDataMap.get(sObjectData.getName()).removeField(fieldData);
            }

            private void updateSOQLText(SalesforceConnection connection) {
                JTextArea queryTextArea = getQueryTextArea(connection);
                Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
                if (sObjectDataMap == null) {
                    return;
                }

                String soqlText = SOQLQueryBuilder.buildQuery(queryTextArea.getText(), sObjectDataMap.values());
                if (soqlText.isBlank()) {
                    removeTab(connection);
                } else {
                    selectTab(connection);
                    queryTextArea.setText(soqlText);
                }
            }
        };
    }
}
