package com.example.springtest.controller;

import com.example.springtest.service.DataMigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DataMigrationService dataMigrationService;

    /**
     * Encrypt all existing unencrypted chat messages
     * POST /api/v1/admin/encrypt-existing-chats
     */
    @PostMapping("/encrypt-existing-chats")
    public ResponseEntity<Map<String, Object>> encryptExistingChats() {
        log.info("POST /api/v1/admin/encrypt-existing-chats - Starting encryption of existing messages");
        
        try {
            long unencryptedCount = dataMigrationService.countUnencryptedMessages();
            log.info("Found {} unencrypted messages", unencryptedCount);
            
            if (unencryptedCount == 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "No unencrypted messages found");
                response.put("encryptedCount", 0);
                response.put("totalUnencrypted", 0);
                return ResponseEntity.ok(response);
            }
            
            int encryptedCount = dataMigrationService.encryptExistingMessages();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Encryption completed");
            response.put("encryptedCount", encryptedCount);
            response.put("totalUnencrypted", unencryptedCount);
            response.put("failedCount", unencryptedCount - encryptedCount);
            
            log.info("Encryption completed: {} out of {} messages encrypted", encryptedCount, unencryptedCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error during encryption migration", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error during encryption: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get count of unencrypted messages
     * GET /api/v1/admin/unencrypted-count
     */
    @GetMapping("/unencrypted-count")
    public ResponseEntity<Map<String, Object>> getUnencryptedCount() {
        log.info("GET /api/v1/admin/unencrypted-count - Checking unencrypted message count");
        
        long count = dataMigrationService.countUnencryptedMessages();
        
        Map<String, Object> response = new HashMap<>();
        response.put("unencryptedCount", count);
        response.put("message", count == 0 ? "All messages are encrypted" : count + " messages need encryption");
        
        return ResponseEntity.ok(response);
    }
}
