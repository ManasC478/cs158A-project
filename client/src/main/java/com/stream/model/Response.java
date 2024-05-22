package com.stream.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> {
    @JsonProperty("success")
    public boolean success;
    @JsonProperty("data")
    public T data;
    @JsonProperty("error")
    public ResponseException error;

    public Response(){}

    public Response(boolean success, T data, ResponseException error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // public boolean getSuccess() {
    //     return success;
    // }

    // public void setSuccess(boolean success) {
    //     this.success = success;
    // }

    // public T getData() {
    //     return data;
    // }

    // public void setData(T data) {
    //     this.data = data;
    // }

    // public ResponseException getException() {
    //     return error;
    // }

    // public void setException(ResponseException error) {
    //     this.error = error;
    // }
}
