package com.example.springtest.repository;

import com.example.springtest.entity.Chat;
import com.example.springtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatFrom(User chatFrom);

    List<Chat> findByChatTo(User chatTo);

    List<Chat> findByChatFromOrChatTo(User chatFrom, User chatTo);
}
