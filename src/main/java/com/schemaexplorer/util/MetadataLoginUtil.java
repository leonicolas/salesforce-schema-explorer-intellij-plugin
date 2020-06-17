package com.schemaexplorer.util;


import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.util.Map;

/**
 * Login utility.
 */
public class MetadataLoginUtil {

    private final static String ORG_DISPLAY_COMMAND = "sfdx force:org:display --json";

    public static MetadataConnection createMetadataConnectionWithAccessToken(final String accessToken, final String serverUrl) throws ConnectionException {
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
