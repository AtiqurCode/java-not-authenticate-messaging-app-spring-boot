package com.example.springtest.mapper;

import com.example.springtest.dto.UserResponse;
import com.example.springtest.dto.UserWithChatsResponse;
import com.example.springtest.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ChatMapper chatMapper;

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserResponse(
                user.getId(),
                user.getUuid(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public UserWithChatsResponse toResponseWithChats(User user) {
        if (user == null) {
            return null;
        }

        List<com.example.springtest.dto.ChatResponse> chats = Stream.concat(
                        user.getSentChats() != null ? user.getSentChats().stream() : Stream.empty(),
                        user.getReceivedChats() != null ? user.getReceivedChats().stream() : Stream.empty()
                )
                .map(chatMapper::toResponse)
                .sorted(Comparator.comparing(com.example.springtest.dto.ChatResponse::getId))
                .collect(Collectors.toList());
        
        return new UserWithChatsResponse(
                user.getId(),
                user.getUuid(),
                user.getName(),
                chats
        );
    }
}
