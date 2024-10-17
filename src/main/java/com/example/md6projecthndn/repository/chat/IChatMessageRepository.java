package com.example.md6projecthndn.repository.chat;

import com.example.md6projecthndn.model.entity.user.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
