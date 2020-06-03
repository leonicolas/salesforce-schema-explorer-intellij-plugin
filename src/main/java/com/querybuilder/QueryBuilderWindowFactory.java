package com.querybuilder;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class QueryBuilderWindowFactory implements ToolWindowFactory {

    private JPanel contentPane;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        QueryBuilderWindow queryBuilderWindow = new QueryBuilderWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(
                queryBuilderWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
