package com.schemaexplorer.view;

import javax.swing.*;

public class ImageResources {

    public static final ImageIcon CONNECTION_OFF = load("/images/connection-off.png");
    public static final ImageIcon CONNECTION_ON = load("/images/connection-on.png");
    //public static final ImageIcon OBJECT_ICON = load("/images/object.png");
    //public static final ImageIcon FIELD_ICON = load("/images/field.png");

    private static ImageIcon load(String resourceFile) {
        return new ImageIcon(ImageResources.class.getResource(resourceFile));
    }
}
