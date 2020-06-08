package com.schemaexplorer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.schemaexplorer.model.FieldData;
import com.schemaexplorer.model.SObjectData;
import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.view.SchemaExplorerWindow;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SchemaExplorerWindowFactory implements ToolWindowFactory {

    private SchemaExplorerWindow schemaExplorerWindow = new SchemaExplorerWindow();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        initializePluginContent(toolWindow);
        this.schemaExplorerWindow.loadSalesforceConnections(getSalesforceConnections());
    }

    private void initializePluginContent(ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(
                schemaExplorerWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private List<SalesforceConnection> getSalesforceConnections() {
        List<SalesforceConnection> connections = List.of(
            new SalesforceConnection("Connection 1"),
            new SalesforceConnection("Connection 2"),
            new SalesforceConnection("Connection 3")
        );

        for(SalesforceConnection connection : connections) {
            for(String sObjectName : new String[] {"Account", "Opportunity", "User"}) {
                SObjectData sObjectData = new SObjectData(connection.getName(), sObjectName);
                sObjectData.addField(new FieldData("Id"));
                sObjectData.addField(new FieldData("Name"));
                sObjectData.addField(new FieldData("Custom_Field__c"));
                connection.addSObjectDataSet(sObjectData);
            }
        };
        return connections;
    }
}
