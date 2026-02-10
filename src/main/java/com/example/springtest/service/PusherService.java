package com.example.springtest.service;

import com.pusher.rest.Pusher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PusherService {

    private final Pusher pusher;

    /**
     * Trigger an event to a specific Pusher channel
     */
    public void triggerEvent(String channel, String event, Map<String, Object> data) {
        try {
            log.info("==========================================");
            log.info("PUSHER TRIGGER ATTEMPT");
            log.info("Channel: {}", channel);
            log.info("Event: {}", event);
            log.info("Data: {}", data);
            log.info("==========================================");
            
            com.pusher.rest.data.Result result = pusher.trigger(channel, event, data);
            
            log.info("==========================================");
            log.info("PUSHER TRIGGER RESULT");
            log.info("Status: {}", result.getStatus());
            log.info("Message: {}", result.getMessage());
            log.info("Channel: {}", channel);
            log.info("Event: {}", event);
            log.info("==========================================");
            
            if (result.getStatus() != com.pusher.rest.data.Result.Status.SUCCESS) {
                log.error("❌ Pusher trigger failed - Status: {}, Message: {}", 
                    result.getStatus(), result.getMessage());
                throw new RuntimeException("Pusher trigger failed: " + result.getMessage());
            }
            
            log.info("✅ Pusher event triggered successfully - Channel: {}, Event: {}", channel, event);
        } catch (Exception e) {
            log.error("❌ Failed to trigger Pusher event", e);
            e.printStackTrace();
            throw new RuntimeException("Failed to trigger Pusher event: " + e.getMessage());
        }
    }

    /**
     * Send a real-time message to a user's channel
     * Each user listens to their own UUID channel (chat-{uuid})
     * Messages are sent only to the recipient's channel
     */
    public void sendMessageToUser(String userUuid, Map<String, Object> messageData) {
        // Public, per-user channel (no auth required). Channel format: chat-{uuid}
        String channelName = "chat-" + userUuid;
        triggerEvent(channelName, "new-message", messageData);
        log.info("Message sent to user channel: {}", channelName);
    }
}
