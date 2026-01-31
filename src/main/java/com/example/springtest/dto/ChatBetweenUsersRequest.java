package com.example.springtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBetweenUsersRequest {
    private String chatFromUuid;
    private String chatToUuid;
}
