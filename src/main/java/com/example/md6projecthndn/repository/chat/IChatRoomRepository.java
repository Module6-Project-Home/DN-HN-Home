package com.example.md6projecthndn.repository.chat;

import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.chat.ChatRoom;
import com.example.md6projecthndn.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    ChatRoom findByUserAndHostAndProperty(User user, User host, Property property);

    ChatRoom findChatRoomById(Long chatRoomId);


    List<ChatRoom> findByHostAndPropertyIn(User host, List<Property> properties);
}
