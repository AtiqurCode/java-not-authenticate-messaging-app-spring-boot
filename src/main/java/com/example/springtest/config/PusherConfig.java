package com.example.springtest.config;

import com.pusher.rest.Pusher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pusher")
@Getter
@Setter
public class PusherConfig {

    private String appId;
    private String key;
    private String secret;
    private String cluster;

    @Bean
    public Pusher pusher() {
        Pusher pusher = new Pusher(appId, key, secret);
        pusher.setCluster(cluster);
        pusher.setEncrypted(true);
        return pusher;
    }
}
