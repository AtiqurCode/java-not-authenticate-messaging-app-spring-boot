package com.example.springtest.dto;

public class PusherAuthRequest {
    private String socket_id;
    private String channel_name;

    public PusherAuthRequest() {}

    public PusherAuthRequest(String socket_id, String channel_name) {
        this.socket_id = socket_id;
        this.channel_name = channel_name;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
}
