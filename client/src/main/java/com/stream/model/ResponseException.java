package com.stream.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseException {
    @JsonProperty("exceptionMessage")
    public String exceptionMessage;
    @JsonProperty("causeMessage")
    public String causeMessage;

    public ResponseException(){}

    public ResponseException(String exceptionMessage, String causeMessage) {
        this.exceptionMessage = exceptionMessage;
        this.causeMessage = causeMessage;
    }
    
    // public String getExceptionMessage() {
    //     return exceptionMessage;
    // }

    // public String getCauseMessage() {
    //     return causeMessage;
    // }

    // public void setExceptionMessage(String exceptionMessage) {
    //     this.exceptionMessage = exceptionMessage;
    // }

    // public void setCauseMessage(String causeMessage) {
    //     this.causeMessage = causeMessage;
    // }
}

