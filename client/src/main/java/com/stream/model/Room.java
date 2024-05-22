package com.stream.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    @JsonProperty("id")
    public long id;
    @JsonProperty("createdAt")
    public String createdAt;

    public Room() {
    }

    public Room(long id, String createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    // public long getId() {
    //     return id;
    // }

    // public LocalDateTime getCreatedAt() {
    //     return createdAt;
    // }

    // public void setId(long id) {
    //     this.id = id;
    // }

    // public void setCreatedAt(LocalDateTime createdAt) {
    //     this.createdAt = createdAt;
    // }

    @Override
    public String toString() {
        return "{id: " + id + ", createdAt: " + createdAt + "}";
    }
}
