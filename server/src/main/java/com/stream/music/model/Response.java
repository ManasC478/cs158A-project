package com.stream.music.model;

public class Response {
    protected boolean success;

    public Response(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }
}
