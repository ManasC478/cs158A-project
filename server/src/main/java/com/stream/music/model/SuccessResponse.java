package com.stream.music.model;

public class SuccessResponse<T> extends Response {
    private T data;
    public SuccessResponse(T data) {
        super(true);
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
