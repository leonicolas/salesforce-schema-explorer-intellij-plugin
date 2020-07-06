package com.schemaexplorer.view;

import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.model.SalesforceObject;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;
import java.util.List;

public interface ConnectionLoadListener extends EventListener {

    default void onConnectionLoad(@NotNull SalesforceConnection connection) {
    }
}
