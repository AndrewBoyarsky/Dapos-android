package com.example.dapos;

import android.view.textclassifier.TextLinks;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Map;

public class HttpClient {
    private static HttpClient instance = new HttpClient();
    private OkHttpClient client = new OkHttpClient();
    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public HttpClient() {

    }

    public static HttpClient getInstance() {
        return instance;
    }

    private static String baseUrl = "http://10.0.2.2:8080/api/rest/v1";

    public String get(Map<String, String> params, String relPath) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/" + relPath).newBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlBuilder.addQueryParameter(param.getKey(), param.getValue());
        }

        String fullUrl = urlBuilder.build().toString();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url(fullUrl)
                .build();
        return doSend(request);
    }

    public String postBody(Object o, String relPath) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/" + relPath).newBuilder();
        String fullUrl = urlBuilder.build().toString();
        Request.Builder builder = new Request.Builder();
        String json = mapper.writeValueAsString(o);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = builder.post(requestBody)
                .url(fullUrl)
                .build();
        return doSend(request);
    }

    private String doSend(Request req) throws IOException {

        Response response = client.newCall(req).execute();
        ResponseBody body = response.body();
        String bodyString = body.string();
        if (response.isSuccessful()) {
            return bodyString;
        } else {
            try {
                JsonNode jsonNode = mapper.readTree(bodyString);
                if (jsonNode.has("errorMessage")) {
                    String s = jsonNode.findValue("errorMessage").asText("Unknown error");
                    throw new IOException(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new IOException(bodyString);
        }
    }

}
