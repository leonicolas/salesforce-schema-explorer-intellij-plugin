package com.schemaexplorer.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.util.Commands;
import com.schemaexplorer.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFConnections {

    private static final Logger logger = LoggerFactory.getLogger(SFConnections.class);
    private final String SFDX_ORG_LIST_COMMAND = "sfdx force:org:list --json";

    public List<SalesforceConnection> getSFDXConnections() {
        List<SalesforceConnection> connectionList = new ArrayList<>();
        Commands command = new Commands(SFDX_ORG_LIST_COMMAND);
        Map<String, Object> map = command.runCommand();

        System.out.println(map.get("result"));
        Map<String, ArrayList> nonScratchOrgs = (Map<String, ArrayList>) map.get("result");

        //Get the list of scratch orgs authed via sfdx cli
        for(Object nonSractchOrg : nonScratchOrgs.get("scratchOrgs")) {
            Map<String, String> orgInfo = (Map<String, String>)nonSractchOrg;
            System.out.println(orgInfo);
            connectionList.add(new SalesforceConnection(orgInfo, Constants.SCRATCH_ORG));
        }

        // Get the list of non-scratch orgs authed via sfdx cli
        for(Object nonSractchOrg : nonScratchOrgs.get("nonScratchOrgs")) {
            Map<String, String> orgInfo = (Map<String, String>)nonSractchOrg;
            System.out.println(orgInfo);
            connectionList.add(new SalesforceConnection(orgInfo, Constants.NON_SCRATCH_ORG));
        }

        return connectionList;
    }
}
