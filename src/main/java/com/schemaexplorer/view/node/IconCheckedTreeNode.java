package com.schemaexplorer.view.node;

import com.intellij.ui.CheckedTreeNode;

import javax.swing.*;
import java.awt.*;

public class IconCheckedTreeNode extends CheckedTreeNode implements IconNode {

    private ImageIcon icon;
    private Rectangle bounds;
    private int row;

    public IconCheckedTreeNode(Object userData) {
        super(userData);
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    public Rectangle getNodeBounds() {
        return bounds;
    }

    public void setNodeBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
