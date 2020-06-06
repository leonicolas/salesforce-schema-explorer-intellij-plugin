package com.schemaexplorer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilderWindowFactory implements ToolWindowFactory {

    private QueryBuilderWindow queryBuilderWindow = new QueryBuilderWindow();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        initializePluginContent(toolWindow);
        this.queryBuilderWindow.loadSObjectData(getSalesforceSObjectData());
    }

    private void initializePluginContent(ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(
                queryBuilderWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private List<SObjectData> getSalesforceSObjectData() {
        return new ArrayList<>() {{
            add(new SObjectData("Account",
                List.of(new SObjectData.FieldData("Id"),
                        new SObjectData.FieldData("Name"),
                        new SObjectData.FieldData("Custom_Field__c")))
            );
            add(new SObjectData("Opportunity",
                List.of(new SObjectData.FieldData("Id"),
                        new SObjectData.FieldData("Name"),
                        new SObjectData.FieldData("Custom_Field__c")))
            );
            add(new SObjectData("User",
                List.of(new SObjectData.FieldData("Id"),
                        new SObjectData.FieldData("Name"),
                        new SObjectData.FieldData("Custom_Field__c")))
            );
        }};
    }
}
