package com.example.md6projecthndn.repository.chat;

import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.ChatRoom;
import com.example.md6projecthndn.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    ChatRoom findByUserAndHostAndProperty(User user, User host, Property property);

    ChatRoom findChatRoomById(Long chatRoomId);
}
