package com.schemaexplorer.view.node;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class IconMutableNode extends DefaultMutableTreeNode implements IconNode {

    private ImageIcon icon;
    private Rectangle bounds;
    private int row;

    public IconMutableNode(Object userData) {
        super(userData);
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public Rectangle getNodeBounds() {
        return bounds;
    }

    public void setNodeBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}
