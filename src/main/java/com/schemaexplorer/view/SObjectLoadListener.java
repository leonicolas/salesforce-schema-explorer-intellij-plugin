package com.schemaexplorer.view;

import com.schemaexplorer.model.SObjectData;
import com.schemaexplorer.model.SalesforceConnection;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface SObjectLoadListener extends EventListener {

    default void onSObjectLoad(@NotNull SObjectData sObjectData) {
    }
}
