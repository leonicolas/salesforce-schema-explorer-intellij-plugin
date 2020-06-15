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
            public void onSObjectLoad(@NotNull SObjectData sObjectData) {
                String accessToken = MetadataLoginUtil.getAccessToken(sObjectData.username);
                System.out.println(accessToken);
                List<Field> fieldDataList = SFMetadata.getFieldData(sObjectData.getName(), accessToken, sObjectData.instanceUrl);
                System.out.println(fieldDataList.size());
                for (Field field : fieldDataList) {
                    sObjectData.addField(new FieldData(field.name));
                }
//            for (String fieldName : new String[]{"Id", "Name", "Custom_Field__c", "CreatedDate"}) {
//                sObjectData.addField(new FieldData(fieldName));
//            }
            }
        };
    }

    private ConnectionLoadListener createConnectionLoadListener() {
        return new ConnectionLoadListener() {
            @Override
            public void onConnectionLoad(@NotNull SalesforceConnection connection) {
//                System.out.println(connection.getAccessToken());
//                System.out.println(connection.getInstanceUrl());
                String accessToken = MetadataLoginUtil.getAccessToken(connection.getUsername());
                System.out.println(accessToken);
                List<SObject> sObjectDataList = SFMetadata.getSObject(accessToken, connection.getInstanceUrl());
                System.out.println(sObjectDataList.size());

                for (SObject sObject : sObjectDataList) {
                    SObjectData sobjectData = new SObjectData(connection.getName(), sObject.name);
                    sobjectData.username = connection.getUsername();
                    sobjectData.instanceUrl = connection.getInstanceUrl();
                    connection.addSObjectData(sobjectData);
                }
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
