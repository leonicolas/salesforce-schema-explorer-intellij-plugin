package com.schemaexplorer.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class ImageResources {

    private static final Logger logger = LoggerFactory.getLogger(ImageResources.class);

    public static final ImageIcon CONNECTION_OFF = load("/images/connection-off.png");
    public static final ImageIcon CONNECTION_ON = load("/images/connection-on.png");
    //public static final ImageIcon OBJECT_ICON = load("/images/object.png");
    //public static final ImageIcon FIELD_ICON = load("/images/field.png");

    private static ImageIcon load(String resourceFile) {
        logger.warn("Loading resource " + resourceFile);
        ImageIcon icon = new ImageIcon(ImageResources.class.getResource(resourceFile));
        icon.setImageObserver(new ImageObserver() {
            @Override
            public boolean imageUpdate(Image image, int infoFlags, int x, int y, int width, int height) {
                logger.warn("Image observer ");
                return false;
            }
        });
        return icon;
    }
}
