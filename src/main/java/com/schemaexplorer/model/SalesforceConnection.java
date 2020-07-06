package com.schemaexplorer.model;

import java.util.Map;

public class SalesforceConnection extends SalesforceObject {

    private Map<String, String> moreInfo;
    private String username;
    private String accessToken;
    private String orgType;
    private String instanceUrl;
    private String alias;
    private boolean loading = false;

    public SalesforceConnection(Map<String, String> moreInfo, String orgType) {
        this.moreInfo = moreInfo;
        this.accessToken = moreInfo.get("accessToken");
        this.orgType = orgType;
        this.alias = moreInfo.get("username");
        this.username = moreInfo.get("username");
        this.instanceUrl = moreInfo.get("instanceUrl");
        this.setName(String.format("%s | %s | %s", this.alias, this.username, orgType));
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

    public String getAlias() {
        return alias;
    }

    public void loadingStarted() {
        this.loading = true;
    }

    public void loadingComplete() {
        this.loading = false;
    }

    public boolean isLoading() {
        return loading;
    }
}
