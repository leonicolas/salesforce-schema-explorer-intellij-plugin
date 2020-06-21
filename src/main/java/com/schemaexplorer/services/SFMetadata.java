package com.schemaexplorer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    private String accessToken;
    private String instanceUrl;
    private ObjectMapper mapper;

    public SFMetadata(String accessToken, String instanceUrl) {
        this.accessToken = accessToken;
        this.instanceUrl = instanceUrl;
    }

    private List<JsonNode> getJSONNodes(String serviceUrl, String key) {
        List<JsonNode> JsonNodeSObjectList = new ArrayList<>();
        try {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final URIBuilder builder = new URIBuilder(this.instanceUrl);
            builder.setPath(serviceUrl);
            System.out.println(builder.getPath());
            System.out.println(builder.build());
            final HttpGet get = new HttpGet(builder.build());
            get.setHeader("Authorization", "Bearer " + this.accessToken);

            final HttpResponse queryResponse = httpclient.execute(get);
            // System.out.println(queryResponse.getEntity().getContent());

            final JsonNode queryResults = this.mapper.readValue(queryResponse.getEntity().getContent(), JsonNode.class);

            // System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryResults));

            JsonNode node = queryResults.get(key);
            System.out.println(node.size());

            JsonNodeSObjectList = this.mapper.readValue(node.toString(), new TypeReference<List<JsonNode>>(){});
            System.out.println(JsonNodeSObjectList.size());
            return JsonNodeSObjectList;

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return JsonNodeSObjectList;
    }

    public List<SObject> getSObject() {
        List<SObject> objectInfoList = new ArrayList<>();

        for(JsonNode sObjectNode : this.getJSONNodes("/services/data/v48.0/sobjects", "sobjects")) {
            try {
                SObject objectInformation = this.mapper.convertValue(sObjectNode, SObject.class);
                objectInfoList.add(objectInformation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objectInfoList;
    }

    public List<Field> getFieldData(String objectAPIName) {
        List<Field> fieldInfoList = new ArrayList<>();

        for(JsonNode field : this.getJSONNodes("/services/data/v48.0/sobjects/"+objectAPIName+"/describe", "fields")) {
            Field fieldInformation = null;
            try {
                fieldInformation = this.mapper.convertValue(field, Field.class);
                fieldInfoList.add(fieldInformation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fieldInfoList;
    }
}
