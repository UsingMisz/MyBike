package com.example.administrator.mybike;

public class MessageLocationEvent {
    public MapLocationInfo getMessage() {
        return message;
    }

    public void setMessage(MapLocationInfo message) {
        this.message = message;
    }

    private MapLocationInfo message;
    public MessageLocationEvent(MapLocationInfo message){
        this.message=message;
    }
}
