package com.example.springtest.service;

import com.example.springtest.entity.Chat;
import com.example.springtest.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataMigrationService {

    private final ChatRepository chatRepository;
    private final EncryptionService encryptionService;

    /**
     * Encrypt all existing unencrypted chat messages
     * This should be run once after deploying encryption feature
     */
    @Transactional
    public int encryptExistingMessages() {
        log.info("Starting encryption of existing chat messages...");
        
        // Find all unencrypted messages
        List<Chat> unencryptedChats = chatRepository.findByIsEncrypted(false);
        
        if (unencryptedChats.isEmpty()) {
            log.info("No unencrypted messages found");
            return 0;
        }
        
        log.info("Found {} unencrypted messages to encrypt", unencryptedChats.size());
        
        int encryptedCount = 0;
        for (Chat chat : unencryptedChats) {
            try {
                // Encrypt the plain text message
                String encryptedMessage = encryptionService.encrypt(chat.getMessage());
                chat.setMessage(encryptedMessage);
                chat.setIsEncrypted(true);
                chatRepository.save(chat);
                encryptedCount++;
                
                if (encryptedCount % 100 == 0) {
                    log.info("Encrypted {} messages so far...", encryptedCount);
                }
            } catch (Exception e) {
                log.error("Failed to encrypt message with id: {}", chat.getId(), e);
                // Continue with next message
            }
        }
        
        log.info("Successfully encrypted {} out of {} messages", encryptedCount, unencryptedChats.size());
        return encryptedCount;
    }

    /**
     * Check how many messages are currently unencrypted
     */
    @Transactional(readOnly = true)
    public long countUnencryptedMessages() {
        return chatRepository.countByIsEncrypted(false);
    }
}
