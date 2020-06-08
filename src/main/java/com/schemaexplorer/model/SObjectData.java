package com.schemaexplorer.model;

import java.util.*;

public class SObjectData {

    private final String connectionName;
    private final String name;
    private Set<FieldData> fields = new HashSet<>();

    public SObjectData(SObjectData sObjectData) {
        this(sObjectData.getConnectionName(), sObjectData.getName());
    }

    public SObjectData(String connectionName, String name) {
        this.connectionName = connectionName;
        this.name = name;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public String getName() {
        return name;
    }

    public void addField(FieldData fieldData) {
        fields.add(fieldData);
    }

    public void removeField(FieldData fieldData) {
        fields.remove(fieldData);
    }

    public Set<FieldData> getFields() {
        return Collections.unmodifiableSet(fields);
    }

    public void clearFields() {
        fields.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SObjectData that = (SObjectData) o;
        return connectionName.equals(that.connectionName) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionName, name);
    }
}