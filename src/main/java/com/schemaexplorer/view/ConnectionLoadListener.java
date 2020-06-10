package com.schemaexplorer.view;

import com.schemaexplorer.model.SalesforceConnection;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public interface ConnectionLoadListener extends EventListener {

    default void onConnectionLoad(@NotNull SalesforceConnection connection) {

    }
}
