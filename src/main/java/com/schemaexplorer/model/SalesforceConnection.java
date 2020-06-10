package com.schemaexplorer.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SalesforceConnection implements Serializable {

    private final String name;
    private final Set<SObjectData> sObjectDataSet = new HashSet<>();

    public SalesforceConnection(String name) {
        this.name = name;
    }

    public void addSObjectData(SObjectData sObjectData) {
        this.sObjectDataSet.add(sObjectData);
    }

    public Set<SObjectData> getSObjectDataSet() {
        return Collections.unmodifiableSet(this.sObjectDataSet);
    }

    public String getName() {
        return name;
    }
}
