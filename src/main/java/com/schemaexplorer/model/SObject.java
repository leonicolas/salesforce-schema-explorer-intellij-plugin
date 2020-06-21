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

    private Set<SalesforceObject> childrenObjects = new HashSet<SalesforceObject>();

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

    public <T extends SalesforceObject> void addChildObject(T childObject) {
        this.childrenObjects.add(childObject);
    }

    public void addChildrenObjects(List<? extends SalesforceObject> childrenObjects) {
        this.childrenObjects.addAll(childrenObjects);
    }

    public <T extends SalesforceObject> void removeChildObject(T childObject) {
        this.childrenObjects.remove(childObject);
    }

    public Set<? extends SalesforceObject> getChildrenObjects() {
        return Collections.unmodifiableSet(this.childrenObjects);
    }

    public Set<? extends SalesforceObject> getSortedChildrenObjects() {
        return ImmutableSortedSet.copyOf(
            Comparator.comparing(SalesforceObject::getName),
            this.childrenObjects
        );
    }

    public void clearChildrenObjects() {
        this.childrenObjects.clear();
    }

    public boolean hasChildrenObjects() {
        return !this.childrenObjects.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionName, getName());
    }
}