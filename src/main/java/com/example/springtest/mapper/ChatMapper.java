package com.example.springtest.mapper;

import com.example.springtest.dto.ChatResponse;
import com.example.springtest.dto.UserResponse;
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
                toUserResponse(chat.getChatFrom()),
                toUserResponse(chat.getChatTo()),
                chat.getCreatedAt()
        );
    }

    private UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUuid(user.getUuid());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
