package com.example.springtest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private Long id;
    private String uuid;
    private String message;
    private Long chatFromId;
    private Long chatToId;
    private UserSummaryResponse chatFrom;
    private UserSummaryResponse chatTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
