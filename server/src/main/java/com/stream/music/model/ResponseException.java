package com.stream.music.model;

public class ResponseException {
    String exceptionMessage, causeMessage;

    public ResponseException(String exceptionMessage, String causeMessage) {
        this.exceptionMessage = exceptionMessage;
        this.causeMessage = causeMessage;
    }
    
    public String exceptionMessage() {
        return exceptionMessage;
    }

    public String causeMessage() {
        return causeMessage;
    }
}
