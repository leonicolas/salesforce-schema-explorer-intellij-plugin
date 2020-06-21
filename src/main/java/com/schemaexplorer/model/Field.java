package com.schemaexplorer.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Field extends SalesforceObject implements Serializable {

    private boolean aggregatable;
    private boolean autoNumber;
    private boolean calculated;
    private boolean calculatedFormula;
    private String compoundFieldName;
    private boolean createable;
    private boolean custom;
    private String defaultValue;
    private String defaultValueFormula;
    private String dependentPicklist;
    private boolean displayLocationInDecimal;
    private boolean encrypted;
    private boolean externalId;
    private String extraTypeInfo;
    private String inlineHelpText;
    private String label;
    private String length;
    private String mask;
    private String maskType;
    private boolean nillable;
    private List<Object> picklistValues;
    private boolean polymorphicForeignKey;
    private int precision;
    private List<Object> referenceTo;
    private String relationshipName;
    private boolean restrictedDelete;
    private boolean restrictedPicklist;
    private int scale;
    private boolean sortable;
    private String type;
    private boolean unique;
    private boolean updateable;

    public Field() {}

    public Field(String name) {
        super(name);
    }

    public boolean isAggregatable() {
        return aggregatable;
    }

    public void setAggregatable(boolean aggregatable) {
        this.aggregatable = aggregatable;
    }

    public boolean isAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(boolean autoNumber) {
        this.autoNumber = autoNumber;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public boolean isCalculatedFormula() {
        return calculatedFormula;
    }

    public void setCalculatedFormula(boolean calculatedFormula) {
        this.calculatedFormula = calculatedFormula;
    }

    public String getCompoundFieldName() {
        return compoundFieldName;
    }

    public void setCompoundFieldName(String compoundFieldName) {
        this.compoundFieldName = compoundFieldName;
    }

    public boolean isCreateable() {
        return createable;
    }

    public void setCreateable(boolean createable) {
        this.createable = createable;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValueFormula() {
        return defaultValueFormula;
    }

    public void setDefaultValueFormula(String defaultValueFormula) {
        this.defaultValueFormula = defaultValueFormula;
    }

    public String getDependentPicklist() {
        return dependentPicklist;
    }

    public void setDependentPicklist(String dependentPicklist) {
        this.dependentPicklist = dependentPicklist;
    }

    public boolean isDisplayLocationInDecimal() {
        return displayLocationInDecimal;
    }

    public void setDisplayLocationInDecimal(boolean displayLocationInDecimal) {
        this.displayLocationInDecimal = displayLocationInDecimal;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isExternalId() {
        return externalId;
    }

    public void setExternalId(boolean externalId) {
        this.externalId = externalId;
    }

    public String getExtraTypeInfo() {
        return extraTypeInfo;
    }

    public void setExtraTypeInfo(String extraTypeInfo) {
        this.extraTypeInfo = extraTypeInfo;
    }

    public String getInlineHelpText() {
        return inlineHelpText;
    }

    public void setInlineHelpText(String inlineHelpText) {
        this.inlineHelpText = inlineHelpText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getMaskType() {
        return maskType;
    }

    public void setMaskType(String maskType) {
        this.maskType = maskType;
    }

    public boolean isNillable() {
        return nillable;
    }

    public void setNillable(boolean nillable) {
        this.nillable = nillable;
    }

    public List<Object> getPicklistValues() {
        return picklistValues;
    }

    public void setPicklistValues(List<Object> picklistValues) {
        this.picklistValues = picklistValues;
    }

    public boolean isPolymorphicForeignKey() {
        return polymorphicForeignKey;
    }

    public void setPolymorphicForeignKey(boolean polymorphicForeignKey) {
        this.polymorphicForeignKey = polymorphicForeignKey;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public List<Object> getReferenceTo() {
        return referenceTo;
    }

    public void setReferenceTo(List<Object> referenceTo) {
        this.referenceTo = referenceTo;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public boolean isRestrictedDelete() {
        return restrictedDelete;
    }

    public void setRestrictedDelete(boolean restrictedDelete) {
        this.restrictedDelete = restrictedDelete;
    }

    public boolean isRestrictedPicklist() {
        return restrictedPicklist;
    }

    public void setRestrictedPicklist(boolean restrictedPicklist) {
        this.restrictedPicklist = restrictedPicklist;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isUpdateable() {
        return updateable;
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}