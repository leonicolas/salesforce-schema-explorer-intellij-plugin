package com.schemaexplorer.view;

import com.schemaexplorer.view.node.IconNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImageResources {

    private static final Logger logger = LoggerFactory.getLogger(ImageResources.class);

    public static final InputStream CONNECTION_OFF_STREAM = load("/images/connection-off.png");
    public static final InputStream CONNECTION_ON_STREAM = load("/images/connection-on.png");
    public static final InputStream CONNECTION_LOADING_STREAM = load("/images/connection-loading.gif");
    //public static final ImageIcon OBJECT_ICON = load("/images/object.png");
    //public static final ImageIcon FIELD_ICON = load("/images/field.png");

    public static ImageIcon createImageIcon(InputStream imageData) {
        try {
            imageData.reset();
            return new ImageIcon(imageData.readAllBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static ImageIcon createAnimatedImageIcon(InputStream imageData, JTree tree, IconNode node) {
        ImageIcon icon = createImageIcon(imageData);
        icon.setImageObserver(new AnimatedGifObserver(tree, node));
        return icon;
    }

    private static InputStream load(String resourceFile) {
        logger.warn("Loading resource " + resourceFile);
        try(InputStream imageData = new BufferedInputStream(ImageResources.class.getResourceAsStream(resourceFile))) {
            return new ByteArrayInputStream(imageData.readAllBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
