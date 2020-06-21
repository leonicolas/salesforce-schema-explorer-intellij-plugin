package com.schemaexplorer.model;

import com.google.common.collect.ImmutableSortedSet;

import java.io.Serializable;
import java.util.*;

public class SObject extends SalesforceObject implements Serializable {

    private String connectionName;
    private String label;
    private Boolean createable;
    private Boolean custom;
    private Boolean customSetting;

    private Set<Field> fields = new HashSet<>();

    public SObject() {
        super();
    }

    public SObject(SObject sObjectData) {
        super(sObjectData.getName());
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getCreateable() {
        return createable;
    }

    public void setCreateable(Boolean createable) {
        this.createable = createable;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Boolean getCustomSetting() {
        return customSetting;
    }

    public void setCustomSetting(Boolean customSetting) {
        this.customSetting = customSetting;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public void addFields(List<Field> fields) {
        this.fields.addAll(fields);
    }

    public void removeField(Field field) {
        this.fields.remove(field);
    }

    public Set<Field> getFields() {
        return Collections.unmodifiableSet(this.fields);
    }

    public Set<Field> getSortedFields() {
        return ImmutableSortedSet.copyOf(
                Comparator.comparing(Field::getName),
                this.fields
        );
    }

    public void clearFields() {
        this.fields.clear();
    }

    public boolean hasFields() {
        return !this.fields.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionName, getName());
    }
}