package com.schemaexplorer.view;

import com.intellij.ui.CheckboxTree;
import com.schemaexplorer.model.FieldData;
import com.schemaexplorer.model.SObjectData;
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
        String nodeText = "";
        if(nodeData instanceof String) {
            nodeText = (String) nodeData;
        }
        else if(nodeData instanceof SalesforceConnection) {
            nodeText = ((SalesforceConnection) nodeData).getName();
        }
        else if(nodeData instanceof SObjectData) {
            nodeText = ((SObjectData) nodeData).getName();
        }
        else if(nodeData instanceof FieldData) {
            nodeText = ((FieldData) nodeData).getName();
        }
        getTextRenderer().append(nodeText);
    }
}