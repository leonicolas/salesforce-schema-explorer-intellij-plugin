package com.schemaexplorer.model;

import java.util.*;

public class SObject extends SalesforceObject {

    private String label;
    private Boolean createable;
    private Boolean custom;
    private Boolean customSetting;

    public SObject() {
        super();
    }

    public SObject(SObject sObjectData) {
        super(sObjectData.getName());
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

    @Override
    public int hashCode() {
        return Objects.hash(getParentName(), getName());
    }
}