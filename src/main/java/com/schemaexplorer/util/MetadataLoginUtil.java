package com.schemaexplorer.util;

//import com.sforce.soap.partner.PartnerConnection;
//import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Login utility.
 */
public class MetadataLoginUtil {

      private final static String ORG_DISPLAY_COMMAND = "sfdx force:org:display --json";
//    private static OrgConnection connectionDetails;
//    private static LoginResult loginResult;
//
//
//    public static void setConnection(OrgConnection connection) {
//        connectionDetails = connection;
//    }
//
//
//
//    public static LoginResult getLoginResult() {
//        return loginResult;
//    }
//
//    public static MetadataConnection login() throws ConnectionException {
//        //final String USERNAME = "ronzheroku@yahoo.com";
//        final String USERNAME = connectionDetails.getUsername();
//        //final String PASSWORD = "jaigurudev86";
//        final String PASSWORD = connectionDetails.getPassword()+connectionDetails.getSecurityToken();
//        //final String URL = "https://login.salesforce.com/services/Soap/u/42.0";
//        final String URL = connectionDetails.getOrgURL()+"/services/Soap/u/42.0";
//        //final LoginResult loginResult = loginToSalesforce(USERNAME, PASSWORD, URL);
//        if(loginResult == null) {
//            loginResult = loginToSalesforce(USERNAME, PASSWORD, URL);
//        }
//        return createMetadataConnection(loginResult);
//    }
//
//    private static MetadataConnection createMetadataConnection(
//            final LoginResult loginResult) throws ConnectionException {
//        final ConnectorConfig config = new ConnectorConfig();
//        System.out.println(loginResult.getMetadataServerUrl());
//        config.setServiceEndpoint(loginResult.getMetadataServerUrl());
//        System.out.println(loginResult.getSessionId());
//        config.setSessionId(loginResult.getSessionId());
//        return new MetadataConnection(config);
//    }
//
//    private static LoginResult loginToSalesforce (final String username, final String password,final String loginUrl) throws ConnectionException {
//        final ConnectorConfig config = new ConnectorConfig();
//        config.setAuthEndpoint(loginUrl);
//        config.setServiceEndpoint(loginUrl);
//        config.setManualLogin(true);
//        return (new PartnerConnection(config)).login(username, password);
//    }

    public static MetadataConnection createMetadataConnection(final String accessToken, final String serverUrl) throws ConnectionException {
        final ConnectorConfig config = new ConnectorConfig();
        System.out.println(serverUrl);
        config.setServiceEndpoint(serverUrl);
        System.out.println(accessToken);
        config.setSessionId(accessToken);
        return new MetadataConnection(config);
    }

    public static String getAccessToken(final String username) {
        Commands command = new Commands(ORG_DISPLAY_COMMAND+" -u "+username);
        Map<String, Object> resultMap = command.runCommand();
        System.out.println(resultMap);
        System.out.println(resultMap.get("result"));
        Map<String, String> infoMap = (Map<String, String>)resultMap.get("result");
        return infoMap.get("accessToken");
    }
}
