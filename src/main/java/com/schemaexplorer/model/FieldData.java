package com.schemaexplorer.model;

import java.util.Objects;

public class FieldData {

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldData fieldData = (FieldData) o;
        return name.equals(fieldData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public FieldData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}