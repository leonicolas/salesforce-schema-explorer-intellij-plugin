package com.schemaexplorer;

import java.util.Collections;
import java.util.List;

public class SObjectData {

    private String name;
    private List<FieldData> fields;

    public SObjectData(String name, List<FieldData> fields) {
        this.name = name;
        if (fields == null) {
            fields = List.of();
        }
        this.fields = Collections.unmodifiableList(fields);
    }

    public String getName() {
        return name;
    }

    public List<FieldData> getFields() {
        return fields;
    }

    public static class FieldData {

        private String name;

        public FieldData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}