package com.schemaexplorer.view;

import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeListener;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.components.JBScrollPane;
import com.schemaexplorer.model.FieldData;
import com.schemaexplorer.model.SObjectData;
import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.util.SOQLQueryBuilder;
import com.schemaexplorer.view.CheckboxTreeCellCustomRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

public class SchemaExplorerWindow {

    private JPanel panel;
    private CheckboxTree sObjectsTree;
    private JTabbedPane queriesTabs;

    private Map<SalesforceConnection, Map<String, SObjectData>> selectedFieldsByConnection = new HashMap<>();

    public JPanel getContent() {
        return panel;
    }

    public void loadSalesforceConnections(List<SalesforceConnection> salesforceConnections) {
        DefaultTreeModel model = (DefaultTreeModel) sObjectsTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();

        rootNode.removeAllChildren();
        salesforceConnections.forEach(connection -> addConnectionNode(rootNode, connection));
        model.reload();
        sObjectsTree.treeDidChange();
        sObjectsTree.expandPath(new TreePath(rootNode));
    }

    private void addConnectionNode(DefaultMutableTreeNode parentNode, SalesforceConnection connection) {
        DefaultMutableTreeNode connectionNode = new DefaultMutableTreeNode(connection);
        parentNode.add(connectionNode);

        connection.getSObjectDataSet().forEach(sObjectData -> {
            CheckedTreeNode sObjectNode = addNewCheckedTreeNode(connectionNode, sObjectData);
            sObjectData.getFields().forEach(fieldData -> addNewCheckedTreeNode(sObjectNode, fieldData));
        });
    }

    private CheckedTreeNode addNewCheckedTreeNode(DefaultMutableTreeNode parentNode, Object userData) {
        CheckedTreeNode node = new CheckedTreeNode(userData);
        node.setChecked(false);
        node.setEnabled(true);
        parentNode.add(node);
        return node;
    }

    private void createUIComponents() {
        this.sObjectsTree = new CheckboxTree(new CheckboxTreeCellCustomRenderer(), null);
        this.sObjectsTree.addCheckboxTreeListener(new CheckboxListener());

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

    private class CheckboxListener implements CheckboxTreeListener {
        @Override
        public void nodeStateChanged(@NotNull CheckedTreeNode node) {
            if (node.getUserObject() instanceof FieldData) {
                updateSelectedField(node);
            }
            else if(node.getUserObject() instanceof SObjectData) {
                updateSelectedSObject(node);
            }
        }

        private void updateSelectedSObject(CheckedTreeNode node) {
            SObjectData sObjectData = (SObjectData) node.getUserObject();
            DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) node.getParent();
            SalesforceConnection connection = (SalesforceConnection) connectionNode.getUserObject();

            if (node.isChecked()) {
                sObjectData.getFields().forEach(fieldData -> addSelectedField(fieldData, connection, sObjectData));
            } else {
                sObjectData.clearFields();
            }

            updateSOQLText(connection);
        }

        private void updateSelectedField(CheckedTreeNode node) {
            FieldData fieldData = (FieldData) node.getUserObject();

            DefaultMutableTreeNode sObjectNode = (DefaultMutableTreeNode) node.getParent();
            SObjectData sObjectData = (SObjectData) sObjectNode.getUserObject();

            DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) sObjectNode.getParent();
            SalesforceConnection connection = (SalesforceConnection) connectionNode.getUserObject();

            if(node.isChecked()) {
                addSelectedField(fieldData, connection, sObjectData);
            } else {
                removeField(fieldData, connection, sObjectData);
            }

            updateSOQLText(connection);
        }

        private void updateSOQLText(SalesforceConnection connection) {
            JTextArea queryTextArea = getQueryTextArea(connection);
            Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
            if(sObjectDataMap == null) {
                return;
            }

            String soqlText = SOQLQueryBuilder.buildQuery(queryTextArea.getText(), sObjectDataMap.values());
            if(soqlText.isBlank()) {
                removeTab(connection);
            } else {
                selectTab(connection);
                queryTextArea.setText(soqlText);
            }
        }

        private void addSelectedField(FieldData fieldData, SalesforceConnection connection, SObjectData sObjectData) {
            if(!selectedFieldsByConnection.containsKey(connection)) {
                selectedFieldsByConnection.put(connection, new HashMap<>());
            }
            Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
            if(!sObjectDataMap.containsKey(sObjectData.getName())) {
                sObjectDataMap.put(sObjectData.getName(), new SObjectData(sObjectData));
            }
            sObjectDataMap.get(sObjectData.getName()).addField(fieldData);
        }

        private void removeField(FieldData fieldData, SalesforceConnection connection, SObjectData sObjectData) {
            Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
            if(!sObjectDataMap.containsKey(sObjectData.getName())) {
                return;
            }
            sObjectDataMap.get(sObjectData.getName()).removeField(fieldData);
        }
    }
}
