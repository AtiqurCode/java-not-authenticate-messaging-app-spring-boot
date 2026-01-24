package com.example.springtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithChatsResponse {
    private Long id;
    private String uuid;
    private String name;
    private List<ChatResponse> chats;
}
