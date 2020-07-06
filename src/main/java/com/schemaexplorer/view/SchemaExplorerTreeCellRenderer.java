package com.schemaexplorer.view;

import com.intellij.ui.CheckboxTree;
import com.schemaexplorer.model.Field;
import com.schemaexplorer.model.SObject;
import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.view.node.IconMutableNode;
import com.schemaexplorer.view.node.SalesforceConnectionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;

public class SchemaExplorerTreeCellRenderer extends CheckboxTree.CheckboxTreeCellRenderer {

    private static final Logger logger = LoggerFactory.getLogger(SchemaExplorerTreeCellRenderer.class);

    @Override
    public void customizeRenderer(JTree tree, Object node, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.customizeRenderer(tree, node, selected, expanded, leaf, row, hasFocus);
        if(!(node instanceof DefaultMutableTreeNode)) {
            return;
        }

        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
        Object nodeData = treeNode.getUserObject();

        if(nodeData instanceof String) {
            getTextRenderer().append((String) nodeData);
        }
        else if(node instanceof SalesforceConnectionNode) {
            renderSalesforceConnection(tree, (SalesforceConnectionNode) node, row);
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

    private void renderSalesforceConnection(JTree tree, SalesforceConnectionNode connectionNode, int row) {
        // Render the text.
        getTextRenderer().append(connectionNode.getSalesforceConnection().getName());
        // Render icon.
        connectionNode.setRow(row);
        getTextRenderer().setIcon(connectionNode.getIcon());
    }
}