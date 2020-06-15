package com.schemaexplorer.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.schemaexplorer.model.Field;
import com.schemaexplorer.model.SObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SFMetadata {

    public static List<SObject> getSObject(final String accessToken, final String instanceUrl) {

        List<SObject> objectInfoList = new ArrayList<>();
        try {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);



//            final String accessToken = MetadataLoginUtil.getLoginResult().getSessionId();
//            final String instanceUrl = MetadataLoginUtil.getLoginResult().getServerUrl().split("\\/services")[0];



            final URIBuilder builder = new URIBuilder(instanceUrl);
            builder.setPath("/services/data/v48.0/sobjects");
            System.out.println(builder.getPath());
            System.out.println(builder.build());
            final HttpGet get = new HttpGet(builder.build());
            get.setHeader("Authorization", "Bearer " + accessToken);

            final HttpResponse queryResponse = httpclient.execute(get);
            // System.out.println(queryResponse.getEntity().getContent());

            final JsonNode queryResults = mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

            // System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults));

            JsonNode objectInfo = queryResults.get("sobjects");
            System.out.println(objectInfo.size());

            List<JsonNode> JsonNodeSObjectList = mapper.readValue(objectInfo.toString(), new TypeReference<List<JsonNode>>(){});
            System.out.println(JsonNodeSObjectList.size());
            for(JsonNode object : JsonNodeSObjectList) {
                SObject objectInformation = mapper.readValue(object.toString(), SObject.class);
                objectInfoList.add(objectInformation);
            }
            return objectInfoList;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return objectInfoList;
    }

    public static List<Field> getFieldData(String objectAPIName, final String accessToken, final String instanceUrl) {
        List<Field> fieldInfoList = new ArrayList<>();

        try {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);



            final URIBuilder builder = new URIBuilder(instanceUrl);
            String url = "/services/data/v48.0/sobjects/"+objectAPIName+"/describe";
            builder.setPath(url);
            final HttpGet get = new HttpGet(builder.build());
            get.setHeader("Authorization", "Bearer " + accessToken);

            final HttpResponse queryResponse = httpclient.execute(get);

            final JsonNode queryResults = mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults));

            JsonNode fieldInfo = queryResults.get("fields");
            System.out.println(fieldInfo.size());
            List<JsonNode> JsonNodefieldInfoList = mapper.readValue(fieldInfo.toString(), new TypeReference<List<JsonNode>>(){});
            System.out.println(JsonNodefieldInfoList.size());
            for(JsonNode field : JsonNodefieldInfoList) {
                Field fieldInformation = mapper.readValue(field.toString(), Field.class);
                fieldInfoList.add(fieldInformation);
            }
            return fieldInfoList;

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return fieldInfoList;
    }
}
