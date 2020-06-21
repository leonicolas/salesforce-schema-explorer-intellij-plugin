package com.schemaexplorer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.schemaexplorer.model.*;
import com.schemaexplorer.services.SFConnections;
import com.schemaexplorer.services.SFMetadata;
import com.schemaexplorer.util.MetadataLoginUtil;
import com.schemaexplorer.view.ConnectionLoadListener;
import com.schemaexplorer.view.SObjectLoadListener;
import com.schemaexplorer.view.SchemaExplorerWindow;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SchemaExplorerWindowFactory implements ToolWindowFactory {

    private SchemaExplorerWindow schemaExplorerWindow = new SchemaExplorerWindow();
    private SFConnections sfConnections = new SFConnections();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        initializePluginContent(toolWindow);
        this.schemaExplorerWindow.addConnectionLoadListener(createConnectionLoadListener());
        this.schemaExplorerWindow.addSObjectLoadListener(createSObjectLoadListener());
        this.schemaExplorerWindow.loadSalesforceConnections(sfConnections.getSFDXConnections());
    }

    private SObjectLoadListener createSObjectLoadListener() {
        return new SObjectLoadListener() {
            @Override
            public void onSObjectLoad(@NotNull SalesforceConnection connection, @NotNull SObject sObject) {
                String accessToken = MetadataLoginUtil.getAccessToken(connection.getUsername());
                System.out.println(accessToken);
                SFMetadata sfMetadata = new SFMetadata(accessToken, connection.getInstanceUrl());
                List<Field> fields = sfMetadata.getFieldData(sObject.getName());
                System.out.println(fields.size());
                sObject.addFields(fields);
            }
        };
    }

    private ConnectionLoadListener createConnectionLoadListener() {
        return new ConnectionLoadListener() {
            @Override
            public void onConnectionLoad(@NotNull SalesforceConnection connection) {
                String accessToken = MetadataLoginUtil.getAccessToken(connection.getUsername());
                System.out.println(accessToken);
                SFMetadata sfMetadata = new SFMetadata(accessToken, connection.getInstanceUrl());
                List<SObject> sObjectDataList = sfMetadata.getSObject();
                System.out.println(sObjectDataList.size());
                connection.addSObjects(sObjectDataList);
            }
        };
    }

    private void initializePluginContent(ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(
                schemaExplorerWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
