package com.schemaexplorer.view;

import com.schemaexplorer.view.node.IconNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class AnimatedGifObserver implements ImageObserver {

    private static final Logger logger = LoggerFactory.getLogger(AnimatedGifObserver.class);

    private final JTree tree;
    private final IconNode node;

    public AnimatedGifObserver(JTree tree, IconNode node) {
        this.tree = tree;
        this.node = node;
    }

    public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height) {
        if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
            Rectangle rect = this.tree.getRowBounds(node.getRow());
            if(rect != null) {
                tree.repaint(rect);
            }
        }
        return (flags & (ALLBITS | ABORT)) == 0;
    }
}