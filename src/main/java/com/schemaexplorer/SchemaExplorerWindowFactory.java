package com.schemaexplorer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.schemaexplorer.model.FieldData;
import com.schemaexplorer.model.SObjectData;
import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.view.ConnectionLoadListener;
import com.schemaexplorer.view.SObjectLoadListener;
import com.schemaexplorer.view.SchemaExplorerWindow;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SchemaExplorerWindowFactory implements ToolWindowFactory {

    private SchemaExplorerWindow schemaExplorerWindow = new SchemaExplorerWindow();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        initializePluginContent(toolWindow);
        this.schemaExplorerWindow.addConnectionLoadListener(createConnectionLoadListener());
        this.schemaExplorerWindow.addSObjectLoadListener(createSObjectLoadListener());
        this.schemaExplorerWindow.loadSalesforceConnections(List.of(
            new SalesforceConnection("Connection 1"),
            new SalesforceConnection("Connection 2"),
            new SalesforceConnection("Connection 3")
        ));
    }

    private SObjectLoadListener createSObjectLoadListener() {
        return new SObjectLoadListener() {
            @Override
            public void onSObjectLoad(@NotNull SObjectData sObjectData) {
            for (String fieldName : new String[]{"Id", "Name", "Custom_Field__c", "CreatedDate"}) {
                sObjectData.addField(new FieldData(fieldName));
            }
            }
        };
    }

    private ConnectionLoadListener createConnectionLoadListener() {
        return new ConnectionLoadListener() {
            @Override
            public void onConnectionLoad(@NotNull SalesforceConnection connection) {
            for (String sObjectName : new String[]{"Account", "Opportunity", "User"}) {
                connection.addSObjectData(new SObjectData(connection.getName(), sObjectName));
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
