package com.example.md6projecthndn.repository.chat;

import com.example.md6projecthndn.model.entity.user.ChatMessage;
import com.example.md6projecthndn.model.entity.user.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomOrderBySentAtDesc(ChatRoom chatRoom);
}
