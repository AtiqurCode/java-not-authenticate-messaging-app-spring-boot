package com.example.springtest.mapper;

import com.example.springtest.dto.ChatResponse;
import com.example.springtest.dto.UserSummaryResponse;
import com.example.springtest.entity.Chat;
import com.example.springtest.entity.User;
import com.example.springtest.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final EncryptionService encryptionService;

    public ChatResponse toResponse(Chat chat) {
        if (chat == null) {
            return null;
        }
        
        // Decrypt message before returning
        String decryptedMessage = encryptionService.decrypt(chat.getMessage());
        
        return new ChatResponse(
                chat.getId(),
                chat.getUuid(),
                decryptedMessage,
                chat.getChatFrom() != null ? chat.getChatFrom().getId() : null,
                chat.getChatTo() != null ? chat.getChatTo().getId() : null,
                toUserSummaryResponse(chat.getChatFrom()),
                toUserSummaryResponse(chat.getChatTo()),
                chat.getCreatedAt(),
                chat.getUpdatedAt()
        );
    }

    private UserSummaryResponse toUserSummaryResponse(User user) {
        if (user == null) {
            return null;
        }

        return new UserSummaryResponse(
                user.getId(),
                user.getUuid(),
                user.getName()
        );
    }
}
