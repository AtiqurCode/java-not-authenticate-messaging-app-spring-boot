package com.example.springtest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Request body for triggering Pusher events
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PusherTriggerRequest {
    private String channel;
    private String event;
    private Object data;
}
