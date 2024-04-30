package com.stream.music.model;

public class ErrorResponse extends Response {
    private ResponseException error;

    public ErrorResponse(ResponseException error) {
        super(false);
        this.error = error;
    }
    
    public ResponseException exceptionMessage() {
        return error;
    }
}
