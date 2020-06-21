package com.schemaexplorer.view;

import com.intellij.ui.CheckboxTree;
import com.schemaexplorer.model.Field;
import com.schemaexplorer.model.SObject;
import com.schemaexplorer.model.SalesforceConnection;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class SchemaExplorerTreeCellRenderer extends CheckboxTree.CheckboxTreeCellRenderer {

    @Override
    public void customizeRenderer(JTree tree, Object node, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.customizeRenderer(tree, node, selected, expanded, leaf, row, hasFocus);
        if(!(node instanceof DefaultMutableTreeNode)) {
            return;
        }

        Object nodeData = ((DefaultMutableTreeNode) node).getUserObject();
        if(nodeData instanceof String) {
            getTextRenderer().append((String) nodeData);
        }
        else if(nodeData instanceof SalesforceConnection) {
            renderSalesforceConnection((SalesforceConnection) nodeData);
        }
        else if(nodeData instanceof SObject) {
            String nodeText = ((SObject) nodeData).getName();
            getTextRenderer().append(nodeText);
        }
        else if(nodeData instanceof Field) {
            String nodeText = ((Field) nodeData).getName();
            getTextRenderer().append(nodeText);
        } else {
            getTextRenderer().append("");
        }
    }

    private void renderSalesforceConnection(SalesforceConnection connection) {
        if(connection.hasObjects()) {
            getTextRenderer().setIcon(ImageResources.CONNECTION_ON);
        } else {
            getTextRenderer().setIcon(ImageResources.CONNECTION_OFF);
        }
        getTextRenderer().append(connection.getName());
    }
}