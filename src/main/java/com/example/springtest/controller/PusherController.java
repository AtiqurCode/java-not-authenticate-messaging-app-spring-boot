package com.example.springtest.controller;

import com.example.springtest.dto.PusherAuthRequest;
import com.example.springtest.service.PusherService;
import com.pusher.rest.Pusher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HexFormat;

@Slf4j
@RestController
@RequestMapping("/api/v1/pusher")
@RequiredArgsConstructor
public class PusherController {

    private final PusherService pusherService;
    private final Pusher pusher;

    /**
     * Authenticate Pusher private channel subscription
     * POST /api/v1/pusher/auth
     */
    @PostMapping("/auth")
    public ResponseEntity<String> authenticateChannel(@RequestBody PusherAuthRequest request) {
        log.info("POST /api/v1/pusher/auth - Authenticate Pusher channel: {}", request.getChannel_name());

        String channelName = request.getChannel_name();
        String socketId = request.getSocket_id();

        if (channelName == null || socketId == null) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Missing required fields: socket_id, channel_name\"}");
        }

        try {
            // Generate auth token manually using HMAC-SHA256
            String authString = socketId + ":" + channelName;
            String appKey = "e74dc87a03e4dbf5d60b"; // Pusher app key
            String secret = "defc8300bb55ed32d94f"; // Pusher secret
            
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(authString.getBytes(StandardCharsets.UTF_8));
            String authSignature = HexFormat.of().formatHex(hmacBytes);
            
            // Response format: { "auth": "<app_key>:<signature>" }
            String response = "{\"auth\":\"" + appKey + ":" + authSignature + "\"}";
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error authenticating Pusher channel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Failed to authenticate channel\"}");
        }
    }

    /**
     * Trigger a Pusher event
     * POST /api/v1/pusher/trigger
     */
    @PostMapping("/trigger")
    public ResponseEntity<Map<String, String>> triggerEvent(@RequestBody Map<String, Object> request) {
        log.info("POST /api/v1/pusher/trigger - Trigger Pusher event");

        String channel = (String) request.get("channel");
        String event = (String) request.get("event");
        Map<String, Object> data = (Map<String, Object>) request.get("data");

        if (channel == null || event == null || data == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing required fields: channel, event, data"));
        }

        try {
            pusherService.triggerEvent(channel, event, data);
            return ResponseEntity.ok(Map.of("status", "Event triggered successfully"));
        } catch (Exception e) {
            log.error("Error triggering Pusher event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Send a message to user's private channel
     * POST /api/v1/pusher/send-message
     */
    @PostMapping("/send-message")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, Object> request) {
        log.info("POST /api/v1/pusher/send-message - Send message via Pusher");

        String userUuid = (String) request.get("userUuid");
        Map<String, Object> messageData = (Map<String, Object>) request.get("messageData");

        if (userUuid == null || messageData == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing required fields: userUuid, messageData"));
        }

        try {
            pusherService.sendMessageToUser(userUuid, messageData);
            return ResponseEntity.ok(Map.of("status", "Message sent successfully"));
        } catch (Exception e) {
            log.error("Error sending message via Pusher", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
