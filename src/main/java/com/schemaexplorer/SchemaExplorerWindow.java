package com.schemaexplorer;

import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeListener;
import com.intellij.ui.CheckedTreeNode;
import com.schemaexplorer.model.FieldData;
import com.schemaexplorer.model.SObjectData;
import com.schemaexplorer.model.SalesforceConnection;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchemaExplorerWindow {

    private JPanel panel;
    private CheckboxTree sObjectsTree;
    private JTabbedPane queriesPanel;

    private Map<SalesforceConnection, Map<String, SObjectData>> selectedFieldsByConnection = new HashMap<>();

    public JPanel getContent() {
        return panel;
    }

    public void loadSalesforceConnections(List<SalesforceConnection> salesforceConnections) {
        DefaultTreeModel model = (DefaultTreeModel) sObjectsTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        rootNode.removeAllChildren();

        for(SalesforceConnection connection : salesforceConnections) {
            addConnectionNode(rootNode, connection);
        }

        model.reload();
        sObjectsTree.treeDidChange();
        sObjectsTree.expandPath(new TreePath(rootNode));
    }

    private void addConnectionNode(DefaultMutableTreeNode parentNode, SalesforceConnection connection) {
        DefaultMutableTreeNode connectionNode = new DefaultMutableTreeNode(connection);
        parentNode.add(connectionNode);

        for(SObjectData sObjectData : connection.getSObjectDataSet()) {
            DefaultMutableTreeNode sObjectNode = new DefaultMutableTreeNode(sObjectData);
            connectionNode.add(sObjectNode);

            for(FieldData fieldData : sObjectData.getFields()) {
                CheckedTreeNode fieldNode = new CheckedTreeNode(fieldData);
                fieldNode.setChecked(false);
                fieldNode.setEnabled(true);
                sObjectNode.add(fieldNode);
            }
        }
    }

    private void createUIComponents() {
        this.sObjectsTree = new CheckboxTree(new CheckboxTreeCellCustomRenderer(), null);
        this.sObjectsTree.addCheckboxTreeListener(new CheckboxListener());

        DefaultTreeModel model = (DefaultTreeModel) this.sObjectsTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        rootNode.setUserObject("Salesforce Schema Explorer");
    }

    private JTextArea getQueryTextArea(SalesforceConnection connection) {
        int tabIndex = queriesPanel.indexOfTab(connection.getName());
        if(tabIndex < 0) {
            queriesPanel.addTab(connection.getName(), new JTextArea());
            tabIndex = queriesPanel.getTabCount() - 1;
        }
        return (JTextArea) queriesPanel.getComponentAt(tabIndex);
    }

    private void removeTab(SalesforceConnection connection) {
        int tabIndex = queriesPanel.indexOfTab(connection.getName());
        if(tabIndex >= 0) {
            queriesPanel.remove(tabIndex);
        }
    }

    private void selectTab(SalesforceConnection connection) {
        int tabIndex = queriesPanel.indexOfTab(connection.getName());
        if(tabIndex >= 0) {
            queriesPanel.setSelectedIndex(tabIndex);
        }
    }

    private class CheckboxListener implements CheckboxTreeListener {
        @Override
        public void nodeStateChanged(@NotNull CheckedTreeNode node) {
            DefaultMutableTreeNode sObjectNode = (DefaultMutableTreeNode) node.getParent();
            SObjectData sObjectData = (SObjectData) sObjectNode.getUserObject();

            FieldData fieldData = (FieldData) node.getUserObject();

            DefaultMutableTreeNode connectionNode = (DefaultMutableTreeNode) sObjectNode.getParent();
            SalesforceConnection connection = (SalesforceConnection) connectionNode.getUserObject();

            // Prepares connection
            if(!selectedFieldsByConnection.containsKey(connection)) {
                selectedFieldsByConnection.put(connection, new HashMap<>());
            }

            // Prepares
            Map<String, SObjectData> sObjectDataMap = selectedFieldsByConnection.get(connection);
            if(!sObjectDataMap.containsKey(sObjectData.getName())) {
                sObjectDataMap.put(sObjectData.getName(), new SObjectData(sObjectData));
            }
            SObjectData selectedSObjectData = sObjectDataMap.get(sObjectData.getName());

            if(node.isChecked()) {
                selectedSObjectData.addField(fieldData);
            } else if(sObjectDataMap.containsKey(sObjectData.getName())) {
                selectedSObjectData.removeField(fieldData);
            }

            JTextArea queryTextArea = getQueryTextArea(connection);
            String soqlText = SOQLQueryBuilder.buildQuery(queryTextArea.getText(), sObjectDataMap.values());
            if(soqlText.isBlank()) {
                removeTab(connection);
            } else {
                selectTab(connection);
                queryTextArea.setText(soqlText);
            }
        }
    }
}
