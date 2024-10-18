package com.example.md6projecthndn.service.chat;

import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.ChatRoom;
import com.example.md6projecthndn.model.entity.user.User;

import java.util.Optional;

public interface IChatRoomService {
    Iterable<ChatRoom> findAll();

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findByDialogId(Long id);

    ChatRoom findByUserAndHostAndProperty(User user, User host, Property property);

    ChatRoom findById(Long chatRoomId);
}
