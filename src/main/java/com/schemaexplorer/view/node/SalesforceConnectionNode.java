package com.schemaexplorer.view.node;

import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.view.ImageResources;

import javax.swing.*;

public class SalesforceConnectionNode extends IconMutableNode {

    private final ImageIcon LOADING_ICON;
    private final ImageIcon CONNECTED_ICON = ImageResources.createImageIcon(ImageResources.CONNECTION_ON_STREAM);
    private final ImageIcon DISCONNECTED_ICON = ImageResources.createImageIcon(ImageResources.CONNECTION_OFF_STREAM);

    public SalesforceConnectionNode(JTree tree, SalesforceConnection salesforceConnection) {
        super(salesforceConnection);
        this.LOADING_ICON = ImageResources.createAnimatedImageIcon(
            ImageResources.CONNECTION_LOADING_STREAM, tree, this
        );
    }

    public SalesforceConnection getSalesforceConnection() {
        return (SalesforceConnection) getUserObject();
    }

    @Override
    public ImageIcon getIcon() {
        SalesforceConnection connection = getSalesforceConnection();
        // Render the icon.
        if(connection.isLoading()) {
            return this.LOADING_ICON;
        } else if(connection.hasChildren()) {
            return this.CONNECTED_ICON;
        } else {
            return this.DISCONNECTED_ICON;
        }
    }
}
