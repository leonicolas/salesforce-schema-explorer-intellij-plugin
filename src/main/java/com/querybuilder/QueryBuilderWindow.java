package com.querybuilder;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckboxTreeBase;
import com.intellij.ui.CheckboxTreeListener;
import com.intellij.ui.CheckedTreeNode;
import org.apache.commons.lang.StringUtils;
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


    public QueryBuilderWindow(ToolWindow toolWindow) {

//        sObjectsTree.setCellRenderer(new CheckboxTree.CheckboxTreeCellRenderer(false) {
//            @Override
//            public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                super.customizeRenderer(tree, value, selected, expanded, leaf, row, hasFocus);
//                String nodeText = "";
//                if(value instanceof CheckedTreeNode) {
//                    nodeText = (String) ((CheckedTreeNode) value).getUserObject();
//                }
//                else if(value instanceof DefaultMutableTreeNode) {
//                    nodeText = (String) ((DefaultMutableTreeNode) value).getUserObject();
//                }
//                getTextRenderer().append(nodeText == null ? "" : nodeText);
//            }
//        });

        // Initializes tree data.
        loadSObjects(new ArrayList<>() {{
            add(new SObject("Account", List.of("Id", "Name", "Custom_Field__c")));
            add(new SObject("Opportunity", List.of("Id", "Name", "Custom_Field__c")));
            add(new SObject("User", List.of("Id", "Name", "Custom_Field__c")));
        }});
    }

    public JPanel getContent() {
        return panel;
    }

    public void loadSObjects(List<SObject> sObjects) {
        DefaultTreeModel model = (DefaultTreeModel) sObjectsTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
        rootNode.removeAllChildren();

        for(SObject node : sObjects) {
            addNode(rootNode, node);
        }

        model.reload();
        sObjectsTree.treeDidChange();
        sObjectsTree.expandPath(new TreePath(rootNode));
    }

    private void addNode(DefaultMutableTreeNode parentNode, SObject sObject) {
        DefaultMutableTreeNode sObjectNode = new DefaultMutableTreeNode(sObject.name);
        parentNode.add(sObjectNode);

        for(String fieldName : sObject.fields) {
            CheckedTreeNode fieldNode = new CheckedTreeNode(fieldName);
            fieldNode.setChecked(false);
            fieldNode.setEnabled(true);
            sObjectNode.add(fieldNode);
        }
    }

    private Map<String, Set<String>> selectedFieldsBySObject = new HashMap<>();

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
                .append(";\n");
        }
        queryTextArea.setText(sb.toString());
    }

    public class SObject {

        private String name;
        private List<String> fields;

        public SObject(String name, List<String> fields) {
            this.name = name;
            if(fields == null) {
                fields = List.of();
            }
            this.fields = Collections.unmodifiableList(fields);
        }

        public String getName() {
            return name;
        }

        public List<String> getFields() {
            return fields;
        }
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
