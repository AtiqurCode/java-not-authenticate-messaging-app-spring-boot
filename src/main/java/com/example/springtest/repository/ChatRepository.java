package com.example.springtest.repository;

import com.example.springtest.entity.Chat;
import com.example.springtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatFrom(User chatFrom);

    List<Chat> findByChatTo(User chatTo);

    List<Chat> findByChatFromOrChatTo(User chatFrom, User chatTo);

    /**
     * Find chats between two specific users only
     * Returns chats where (user1 → user2) OR (user2 → user1)
     */
    @Query("SELECT c FROM Chat c WHERE " +
           "(c.chatFrom = :user1 AND c.chatTo = :user2) OR " +
           "(c.chatFrom = :user2 AND c.chatTo = :user1) " +
           "ORDER BY c.createdAt ASC")
    List<Chat> findChatsBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    /**
     * Find all chats by encryption status
     */
    List<Chat> findByIsEncrypted(Boolean isEncrypted);

    /**
     * Count chats by encryption status
     */
    long countByIsEncrypted(Boolean isEncrypted);
}
