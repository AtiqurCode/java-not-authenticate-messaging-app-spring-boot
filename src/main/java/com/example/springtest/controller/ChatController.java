package com.example.springtest.controller;

import com.example.springtest.dto.ChatRequest;
import com.example.springtest.dto.ChatResponse;
import com.example.springtest.dto.ChatFilterRequest;
import com.example.springtest.dto.ChatBetweenUsersRequest;
import com.example.springtest.dto.ChatUpdateRequest;
import com.example.springtest.service.ChatService;
import com.example.springtest.service.PusherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final PusherService pusherService;

    /**
     * Create a new chat message
     * POST /api/v1/chats
     */
    @PostMapping
    public ResponseEntity<ChatResponse> createChat(@RequestBody ChatRequest chatRequest) {
        log.info("POST /api/v1/chats - Create chat message from {} to {}",
                 chatRequest.getChatFromUuid(), chatRequest.getChatToUuid());

        // Save message to database
        ChatResponse response = chatService.createChat(chatRequest);

        // Trigger Pusher event to recipient only
        try {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("id", response.getId());
            messageData.put("chatFromUuid", response.getChatFrom().getUuid());
            messageData.put("chatToUuid", response.getChatTo().getUuid());
            messageData.put("message", response.getMessage());
            messageData.put("senderName", response.getChatFrom().getName());
            // Convert LocalDateTime to ISO string to avoid Gson reflection issues
            messageData.put("createdAt", response.getCreatedAt() != null ? response.getCreatedAt().toString() : null);

            // Send to recipient's channel only (toUUID)
            pusherService.sendMessageToUser(chatRequest.getChatToUuid(), messageData);

            log.info("Pusher event triggered to recipient {} for message ID: {}",
                     chatRequest.getChatToUuid(), response.getId());
        } catch (Exception e) {
            log.error("Failed to trigger Pusher event, but message was saved", e);
            // Don't fail the response - message is already saved
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Test Pusher connectivity
     * POST /api/v1/chats/test-pusher/{userUuid}
     */
    @PostMapping("/test-pusher/{userUuid}")
    public ResponseEntity<Map<String, String>> testPusher(@PathVariable String userUuid) {
        log.info("========== TEST PUSHER ==========");
        log.info("Testing Pusher event to user: {}", userUuid);
        
        try {
            Map<String, Object> testData = new HashMap<>();
            testData.put("test", true);
            testData.put("message", "This is a test message");
            testData.put("timestamp", System.currentTimeMillis());
            
            String channelName = "chat-" + userUuid;
            log.info("Sending test event to channel: {}", channelName);
            
            pusherService.triggerEvent(channelName, "test-event", testData);
            
            log.info("✓ Test event sent successfully");
            log.info("========== END TEST PUSHER ==========");
            
            return ResponseEntity.ok(Map.of("status", "Test event sent to " + channelName));
        } catch (Exception e) {
            log.error("❌ Test Pusher failed", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get all chats
     * GET /api/v1/chats
     */
    @GetMapping
    public ResponseEntity<List<ChatResponse>> getAllChats() {
        log.info("GET /api/v1/chats - Retrieve all chats");
        List<ChatResponse> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    /**
     * Get chat by ID
     * GET /api/v1/chats/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable Long id) {
        log.info("GET /api/v1/chats/{} - Retrieve chat by ID", id);
        ChatResponse chat = chatService.getChatById(id);
        return ResponseEntity.ok(chat);
    }

    /**
     * Get all chats sent by a user
     * POST /api/v1/chats/sent
     */
    @PostMapping("/sent")
    public ResponseEntity<List<ChatResponse>> getChatsBySender(@RequestBody ChatFilterRequest filterRequest) {
        log.info("POST /api/v1/chats/sent - Retrieve sent chats for user: {}", filterRequest.getUserUuid());
        List<ChatResponse> chats = chatService.getChatsBySender(filterRequest.getUserUuid());
        return ResponseEntity.ok(chats);
    }

    /**
     * Get all chats received by a user
     * POST /api/v1/chats/received
     */
    @PostMapping("/received")
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(@RequestBody ChatFilterRequest filterRequest) {
        log.info("POST /api/v1/chats/received - Retrieve received chats for user: {}", filterRequest.getUserUuid());
        List<ChatResponse> chats = chatService.getChatsByReceiver(filterRequest.getUserUuid());
        return ResponseEntity.ok(chats);
    }

    /**
     * Get all chats between two users
     * POST /api/v1/chats/between
     */
    @PostMapping("/between")
    public ResponseEntity<List<ChatResponse>> getChatsBetweenUsers(@RequestBody ChatBetweenUsersRequest request) {
        log.info("POST /api/v1/chats/between - Retrieve chats between users: {} and {}",
            request.getChatFromUuid(), request.getChatToUuid());
        List<ChatResponse> chats = chatService.getChatsBetweenUsers(request.getChatFromUuid(), request.getChatToUuid());
        return ResponseEntity.ok(chats);
    }

    /**
     * Update chat message
     * PUT /api/v1/chats/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatResponse> updateChat(
            @PathVariable Long id,
            @RequestBody ChatUpdateRequest updateRequest) {
        log.info("PUT /api/v1/chats/{} - Update chat message", id);
        
        ChatResponse response = chatService.updateChat(id,
                new ChatRequest(updateRequest.getMessage(), null, null));
        
        // Notify both users via Pusher about the update
        try {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("id", response.getId());
            updateData.put("messageId", response.getId());
            updateData.put("message", response.getMessage());
            updateData.put("action", "edit-message");
            updateData.put("chatFromUuid", response.getChatFrom().getUuid());
            updateData.put("chatToUuid", response.getChatTo().getUuid());
            updateData.put("updatedAt", response.getUpdatedAt() != null ? response.getUpdatedAt().toString() : null);
            
            String recipientChannelName = "chat-" + response.getChatTo().getUuid();
            
            log.info("========== EDIT MESSAGE EVENT ==========");
            log.info("Message ID: {}", response.getId());
            log.info("Message Sender UUID: {}", response.getChatFrom().getUuid());
            log.info("Message Recipient UUID (chatTo): {}", response.getChatTo().getUuid());
            log.info("Sending to RECIPIENT channel ONLY: {}", recipientChannelName);
            log.info("New Message Text: {}", response.getMessage());
            log.info("Event Data: {}", updateData);
            log.info("==========================================");
            
            // Notify recipient only
            log.info("➡️ Triggering edit-message to RECIPIENT...");
            pusherService.triggerEvent(recipientChannelName, "edit-message", updateData);
            log.info("✅ Edit event delivered to RECIPIENT channel");
            log.info("========== END EDIT MESSAGE EVENT ==========");
            
        } catch (Exception e) {
            log.error("❌ Failed to send Pusher update event", e);
            e.printStackTrace();
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete chat
     * DELETE /api/v1/chats/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.info("DELETE /api/v1/chats/{} - Delete chat", id);

        // Get chat details before deleting for Pusher notification
        try {
            ChatResponse chat = chatService.getChatById(id);

            // Delete the message
            chatService.deleteChat(id);

            // Notify both users via Pusher
            try {
                Map<String, Object> deleteData = new HashMap<>();
                deleteData.put("id", id);
                deleteData.put("messageId", id);
                deleteData.put("action", "delete-message");
                deleteData.put("chatFromUuid", chat.getChatFrom().getUuid());
                deleteData.put("chatToUuid", chat.getChatTo().getUuid());
                
                String recipientChannelName = "chat-" + chat.getChatTo().getUuid();
                
                log.info("========== DELETE MESSAGE EVENT ==========");
                log.info("Message ID: {}", id);
                log.info("Message Sender UUID: {}", chat.getChatFrom().getUuid());
                log.info("Message Recipient UUID (chatTo): {}", chat.getChatTo().getUuid());
                log.info("Sending delete-message Pusher event to RECIPIENT channel ONLY: {}", recipientChannelName);
                log.info("Event data: {}", deleteData);

                // Notify recipient only
                pusherService.triggerEvent(recipientChannelName, "delete-message", deleteData);
                log.info("✓ Delete event sent to recipient: {}", chat.getChatTo().getUuid());
                log.info("========== END DELETE MESSAGE EVENT ==========");
            } catch (Exception e) {
                log.error("❌ Failed to send Pusher delete event", e);
                e.printStackTrace();
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting chat {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
