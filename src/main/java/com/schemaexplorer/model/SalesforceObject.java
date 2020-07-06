package com.schemaexplorer.model;

import com.google.common.collect.ImmutableSortedSet;

import java.io.Serializable;
import java.util.*;

public abstract class SalesforceObject implements Serializable {

    private String name;
    private String parentName;

    private Set<SalesforceObject> children = new HashSet<SalesforceObject>();

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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void addChild(SalesforceObject childObject) {
        this.children.add(childObject);
    }

    public void addChildren(List<? extends SalesforceObject> childrenObjects) {
        childrenObjects.forEach(child -> {
            child.setParentName(this.getName());
            addChild(child);
        });
    }

    public Set<SalesforceObject> getChildrenDataSet() {
        return Collections.unmodifiableSet(this.children);
    }

    public Set<SalesforceObject> getSortedChildrenDataSet() {
        return ImmutableSortedSet.copyOf(
                Comparator.comparing(SalesforceObject::getName),
                this.children
        );
    }

    public Boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public void clearChildren() {
        this.children.clear();
    }

    public void removeChild(SalesforceObject childToRemove) {
        this.children.remove(childToRemove);
    }
}
