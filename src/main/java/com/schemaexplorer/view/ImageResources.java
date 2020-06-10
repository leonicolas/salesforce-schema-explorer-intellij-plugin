package com.schemaexplorer.view;

import javax.swing.*;

public class ImageResources {

    public static final ImageIcon CONNECTION_CONNECTED_ICON = load("/images/connection.png");
    public static final ImageIcon CONNECTION_DISCONNECTED_ICON = load("/images/connected.png");
    public static final ImageIcon OBJECT_ICON = load("/images/object.png");
    public static final ImageIcon FIELD_ICON = load("/images/field.png");

    private static ImageIcon load(String resourceFile) {
        return new ImageIcon(ImageResources.class.getResource(resourceFile));
    }
}
