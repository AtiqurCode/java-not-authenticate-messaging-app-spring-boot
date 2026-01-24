package com.example.springtest.mapper;

import com.example.springtest.dto.ChatResponse;
import com.example.springtest.dto.UserSummaryResponse;
import com.example.springtest.entity.Chat;
import com.example.springtest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatResponse toResponse(Chat chat) {
        if (chat == null) {
            return null;
        }
        
        return new ChatResponse(
                chat.getId(),
                chat.getUuid(),
                chat.getMessage(),
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
