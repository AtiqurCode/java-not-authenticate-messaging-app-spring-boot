package com.example.springtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_from", nullable = false)
    private User chatFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_to", nullable = false)
    private User chatTo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
