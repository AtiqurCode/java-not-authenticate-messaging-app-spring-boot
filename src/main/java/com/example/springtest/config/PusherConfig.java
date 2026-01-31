package com.example.springtest.config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {

    @Bean
    public Pusher pusher() {
        Pusher pusher = new Pusher("2106469", "e74dc87a03e4dbf5d60b", "defc8300bb55ed32d94f");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);
        return pusher;
    }
}
