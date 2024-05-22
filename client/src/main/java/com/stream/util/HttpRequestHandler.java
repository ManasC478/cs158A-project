package com.stream.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stream.model.Response;
import com.stream.model.ResponseException;

import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;

public class HttpRequestHandler {
    private Builder builder;
    private static HttpClient client = HttpClient.newHttpClient();

    public static enum Method {
        GET, POST, PUT, DELETE
    }

    public HttpRequestHandler(String url) {
        builder = HttpRequest.newBuilder()
                .uri(URI.create(url));
    }
    
    public void setMethod(Method method) throws IllegalArgumentException {
        if (method == Method.POST || method == Method.PUT) {
            throw new IllegalArgumentException("Method requires body publisher");
        }
        if (method == Method.GET) {
            builder = builder.GET();
        } else if (method == Method.DELETE) {
            builder = builder.DELETE();
        }
    }

    public void setMethod(Method method, BodyPublisher bodyPublisher) throws IllegalArgumentException {
        if (method == Method.GET || method == Method.DELETE) {
            throw new IllegalArgumentException("Method cannot have body publisher");
        }
        if (method == Method.POST) {
            builder = builder.POST(bodyPublisher);
        } else if (method == Method.PUT) {
            builder = builder.PUT(bodyPublisher);
        }
    }

    public void setHeader(String header, String value) {
        builder.setHeader(header, value);
    }

    public String send() throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(builder.build(), BodyHandlers.ofString());
        return response.body();
    }
    
    public void printError(ResponseException e) {
        System.out.println("Exception message: "+e.exceptionMessage);
        System.out.println("Cause message: "+e.causeMessage);
    }
}
