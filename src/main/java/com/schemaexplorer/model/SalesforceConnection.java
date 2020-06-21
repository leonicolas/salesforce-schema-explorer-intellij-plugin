package com.schemaexplorer.model;

import com.google.common.collect.ImmutableSortedSet;

import java.io.Serializable;
import java.util.*;

public class SalesforceConnection implements Serializable {

    private final Map<String, String> moreInfo;
    private final String name;
    private final String username;
    private final String accessToken;
    private final String orgType;
    private final String instanceUrl;
    private final Set<SObject> sObjectDataSet = new HashSet<>();

    public SalesforceConnection(Map<String, String> moreInfo, String orgType) {
        this.moreInfo = moreInfo;
        this.name = moreInfo.get("alias")+" | "+this.moreInfo.get("username")+" | "+orgType;
        this.accessToken = moreInfo.get("accessToken");
        this.orgType = orgType;
        this.username = moreInfo.get("username");
        this.instanceUrl = moreInfo.get("instanceUrl");
    }

    public void addSObject(SObject sObject) {
        this.sObjectDataSet.add(sObject);
    }

    public void addSObjects(List<SObject> sObjects) {
        sObjects.forEach(sObject -> {
            sObject.setConnectionName(this.getName());
            addSObject(sObject);
        });
    }

    public Set<SObject> getSObjectDataSet() {
        return Collections.unmodifiableSet(this.sObjectDataSet);
    }

    public Set<SObject> getSortedSObjectDataSet() {
        return ImmutableSortedSet.copyOf(
            Comparator.comparing(SObject::getName),
            this.sObjectDataSet
        );
    }

    public Boolean hasObjects() {
        return !this.sObjectDataSet.isEmpty();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getMoreInfo() {
        return moreInfo;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public String getOrgType() {
        return orgType;
    }
}
