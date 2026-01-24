package com.example.springtest.controller;

import com.example.springtest.dto.ChatRequest;
import com.example.springtest.dto.ChatResponse;
import com.example.springtest.dto.ChatFilterRequest;
import com.example.springtest.dto.ChatBetweenUsersRequest;
import com.example.springtest.dto.ChatUpdateRequest;
import com.example.springtest.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Create a new chat message
     * POST /api/v1/chats
     */
    @PostMapping
    public ResponseEntity<ChatResponse> createChat(@RequestBody ChatRequest chatRequest) {
        log.info("POST /api/v1/chats - Create chat message");
        ChatResponse response = chatService.createChat(chatRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
                request.getUserUuid1(), request.getUserUuid2());
        List<ChatResponse> chats = chatService.getChatsBetweenUsers(request.getUserUuid1(), request.getUserUuid2());
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
        return ResponseEntity.ok(response);
    }

    /**
     * Delete chat
     * DELETE /api/v1/chats/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.info("DELETE /api/v1/chats/{} - Delete chat", id);
        chatService.deleteChat(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
