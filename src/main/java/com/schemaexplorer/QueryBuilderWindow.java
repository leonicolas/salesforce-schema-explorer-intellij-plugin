package com.schemaexplorer;

import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeListener;
import com.intellij.ui.CheckedTreeNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.*;

public class QueryBuilderWindow {

    private JPanel panel;
    private JTextArea queryTextArea;
    private CheckboxTree sObjectsTree;
    private Map<String, Set<String>> selectedFieldsBySObject = new HashMap<>();

    public JPanel getContent() {
        return panel;
    }

    public void loadSObjectData(List<SObjectData> sObjectsData) {
        DefaultTreeModel model = (DefaultTreeModel) sObjectsTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        rootNode.removeAllChildren();

        for(SObjectData node : sObjectsData) {
            addNode(rootNode, node);
        }

        model.reload();
        sObjectsTree.treeDidChange();
        sObjectsTree.expandPath(new TreePath(rootNode));
    }

    private void addNode(DefaultMutableTreeNode parentNode, SObjectData sObjectData) {
        DefaultMutableTreeNode sObjectNode = new DefaultMutableTreeNode(sObjectData.getName());
        parentNode.add(sObjectNode);

        for(SObjectData.FieldData fieldData : sObjectData.getFields()) {
            CheckedTreeNode fieldNode = new CheckedTreeNode(fieldData.getName());
            fieldNode.setChecked(false);
            fieldNode.setEnabled(true);
            sObjectNode.add(fieldNode);
        }
    }

    private void createUIComponents() {
        sObjectsTree = new CheckboxTree(new CheckboxTreeCellCustomRenderer(), null);
        sObjectsTree.addCheckboxTreeListener(new CheckboxTreeListener() {
            @Override
            public void nodeStateChanged(@NotNull CheckedTreeNode node) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            String sObjectName = (String) parentNode.getUserObject();
            String fieldName = (String) node.getUserObject();

            if(node.isChecked()) {
                if(!selectedFieldsBySObject.containsKey(sObjectName)) {
                    selectedFieldsBySObject.put(sObjectName, new HashSet<>());
                }
                selectedFieldsBySObject.get(sObjectName).add(fieldName);
            } else if(selectedFieldsBySObject.containsKey(sObjectName)) {
                selectedFieldsBySObject.get(sObjectName).remove(fieldName);
            }
            buildQuery();
            }
        });
    }

    private void buildQuery() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Set<String>> sObjectData : selectedFieldsBySObject.entrySet()) {
            if(sObjectData.getValue().isEmpty()) {
                continue;
            }

            sb.append("SELECT ")
                .append(String.join(", ", sObjectData.getValue()))
                .append("\nFROM ")
                .append(sObjectData.getKey())
                .append(";\n\n");
        }
        queryTextArea.setText(sb.toString());
    }

    private class CheckboxTreeCellCustomRenderer extends CheckboxTree.CheckboxTreeCellRenderer {
        @Override
        public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.customizeRenderer(tree, value, selected, expanded, leaf, row, hasFocus);

            String nodeText = null;
            if(value instanceof CheckedTreeNode) {
                nodeText = (String) ((CheckedTreeNode) value).getUserObject();
            }
            else if(value instanceof DefaultMutableTreeNode) {
                nodeText = (String) ((DefaultMutableTreeNode) value).getUserObject();
            }

            if(nodeText != null) {
                getTextRenderer().append(nodeText);
            }
        }
    }
}
