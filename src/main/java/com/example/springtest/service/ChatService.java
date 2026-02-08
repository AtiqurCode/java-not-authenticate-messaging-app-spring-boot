package com.example.springtest.service;

import com.example.springtest.dto.ChatRequest;
import com.example.springtest.dto.ChatResponse;
import com.example.springtest.entity.Chat;
import com.example.springtest.entity.User;
import com.example.springtest.exception.ResourceNotFoundException;
import com.example.springtest.mapper.ChatMapper;
import com.example.springtest.repository.ChatRepository;
import com.example.springtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;
    private final EncryptionService encryptionService;

    /**
     * Create a new chat message
     */
    public ChatResponse createChat(ChatRequest chatRequest) {
        log.info("Creating new chat message from: {} to: {}", 
                chatRequest.getChatFromUuid(), chatRequest.getChatToUuid());
        
        validateChatRequest(chatRequest);
        
        User chatFromUser = findUserByUuid(chatRequest.getChatFromUuid());
        User chatToUser = findUserByUuid(chatRequest.getChatToUuid());
        
        Chat chat = new Chat();
        // Encrypt message before saving
        String encryptedMessage = encryptionService.encrypt(chatRequest.getMessage());
        chat.setMessage(encryptedMessage);
        chat.setIsEncrypted(true);
        chat.setChatFrom(chatFromUser);
        chat.setChatTo(chatToUser);

        Chat savedChat = chatRepository.save(chat);
        log.info("Chat message created with id: {}", savedChat.getId());
        
        return chatMapper.toResponse(savedChat);
    }

    /**
     * Get all chats
     */
    @Transactional(readOnly = true)
    public List<ChatResponse> getAllChats() {
        log.debug("Fetching all chat messages");
        return chatRepository.findAll()
                .stream()
                .map(chatMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get chat by ID
     */
    @Transactional(readOnly = true)
    public ChatResponse getChatById(Long id) {
        log.debug("Fetching chat with id: {}", id);
        Chat chat = findChatById(id);
        return chatMapper.toResponse(chat);
    }

    /**
     * Get all chats sent by a user
     */
    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsBySender(String userUuid) {
        log.debug("Fetching sent chats for user: {}", userUuid);
        User user = findUserByUuid(userUuid);
        return chatRepository.findByChatFrom(user)
                .stream()
                .map(chatMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all chats received by a user
     */
    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiver(String userUuid) {
        log.debug("Fetching received chats for user: {}", userUuid);
        User user = findUserByUuid(userUuid);
        return chatRepository.findByChatTo(user)
                .stream()
                .map(chatMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all chats between two users (both sent and received)
     * Only returns messages exchanged directly between user1 and user2
     */
    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsBetweenUsers(String userUuid1, String userUuid2) {
        log.debug("Fetching chats between users: {} and {}", userUuid1, userUuid2);
        User user1 = findUserByUuid(userUuid1);
        User user2 = findUserByUuid(userUuid2);
        
        // Use repository method to get only chats between these two specific users
        return chatRepository.findChatsBetweenUsers(user1, user2)
                .stream()
                .map(chatMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update chat message
     */
    public ChatResponse updateChat(Long id, ChatRequest chatRequest) {
        log.info("Updating chat with id: {}", id);
        Chat chat = findChatById(id);
        validateMessageNotEmpty(chatRequest.getMessage());
        
        // Encrypt message before saving
        String encryptedMessage = encryptionService.encrypt(chatRequest.getMessage());
        chat.setMessage(encryptedMessage);
        chat.setIsEncrypted(true);
        Chat updatedChat = chatRepository.save(chat);
        
        log.info("Chat with id: {} updated successfully", id);
        return chatMapper.toResponse(updatedChat);
    }

    /**
     * Delete chat
     */
    public void deleteChat(Long id) {
        log.info("Deleting chat with id: {}", id);
        Chat chat = findChatById(id);
        chatRepository.delete(chat);
        log.info("Chat with id: {} deleted successfully", id);
    }

    // ====================== Private Helper Methods ======================

    /**
     * Find chat by ID or throw exception
     */
    private Chat findChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found with id: " + id));
    }

    /**
     * Find user by UUID or throw exception
     */
    private User findUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with UUID: " + uuid));
    }

    /**
     * Validate chat request
     */
    private void validateChatRequest(ChatRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Chat request cannot be null");
        }
        validateMessageNotEmpty(request.getMessage());
        if (request.getChatFromUuid() == null || request.getChatFromUuid().isBlank()) {
            throw new IllegalArgumentException("chatFromUuid cannot be empty");
        }
        if (request.getChatToUuid() == null || request.getChatToUuid().isBlank()) {
            throw new IllegalArgumentException("chatToUuid cannot be empty");
        }
    }

    /**
     * Validate message is not empty
     */
    private void validateMessageNotEmpty(String message) {
        if (message == null || message.trim().isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
    }
}
