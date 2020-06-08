package com.schemaexplorer;

import com.schemaexplorer.model.SObjectData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SOQLQueryBuilder {

    public static String buildQuery(String currentSOQLText, Collection<SObjectData> sObjectDataSet) {
        StringBuilder sb = new StringBuilder();
        for(SObjectData sObjectData : sObjectDataSet) {
            if(sObjectData.getFields().isEmpty()) {
                continue;
            }

            Set<String> fieldsNames = new HashSet<>();
            sObjectData.getFields().forEach(fieldData -> fieldsNames.add(fieldData.getName()));

            Pattern soqlPattern = Pattern.compile(String.format("(?<=FROM %s)[^;]*", sObjectData.getName()));
            Matcher soqlMatcher = soqlPattern.matcher(currentSOQLText);
            String currentSoqlData = soqlMatcher.find() ? soqlMatcher.group() : "";
            sb.append("SELECT ")
                .append(String.join(", ", fieldsNames))
                .append("\nFROM ")
                .append(sObjectData.getName())
                .append(currentSoqlData)
                .append(";\n\n");
        }
        return sb.toString();
    }
}
