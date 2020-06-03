package com.querybuilder;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import org.scijava.swing.checkboxtree.CheckBoxNodeData;
import org.scijava.swing.checkboxtree.CheckBoxNodeEditor;
import org.scijava.swing.checkboxtree.CheckBoxNodeRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryBuilderWindow {

    private JPanel panel;
    private JTextArea queryTextArea;
    private Tree sObjectsTree;

    public QueryBuilderWindow(ToolWindow toolWindow) {
        // Initializes checkbox node rerender.
        final CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
        sObjectsTree.setCellRenderer(renderer);

        // Initializes checkbox node editor.
        final CheckBoxNodeEditor editor = new CheckBoxNodeEditor(sObjectsTree);
        sObjectsTree.setCellEditor(editor);
        sObjectsTree.setEditable(true);

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

        for(String field : sObject.fields) {
            CheckBoxNodeData fieldNode = new CheckBoxNodeData(field, false);
            sObjectNode.add(new DefaultMutableTreeNode(fieldNode));
        }
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
}
