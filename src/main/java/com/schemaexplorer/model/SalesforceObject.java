package com.schemaexplorer.model;

public abstract class SalesforceObject {

    private String name;

    public SalesforceObject() {
    }

    public SalesforceObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
