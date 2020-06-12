package com.schemaexplorer.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schemaexplorer.model.SalesforceConnection;
import com.schemaexplorer.util.Constants;

public class SFConnections {
    public List<SalesforceConnection> getSFDXConnections() {
        List<SalesforceConnection> connectionList = new ArrayList<>();
        try {
            String s;
            StringBuilder json = new StringBuilder();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map;
            Process p = Runtime.getRuntime().exec("sfdx force:org:list --json");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                json.append(s);
            }
            System.out.println(json);
            map = mapper.readValue(json.toString(), Map.class);
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

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            //System.exit(0);
            return connectionList;
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            //System.exit(-1);
        }
        return connectionList;
    }
}
