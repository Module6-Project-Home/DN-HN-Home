package com.example.md6projecthndn.service.chat;

import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.chat.ChatRoom;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.repository.chat.IChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomService implements IChatRoomService {
    private final IChatRoomRepository iChatRoomRepository;
    public ChatRoomService(IChatRoomRepository iChatRoomRepository){
        this.iChatRoomRepository = iChatRoomRepository;
    }

    @Override
    public Iterable<ChatRoom> findAll() {
        return iChatRoomRepository.findAll();
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return iChatRoomRepository.save(chatRoom);
    }

    public Optional<ChatRoom> findByDialogId(Long id) {
        return iChatRoomRepository.findById(id);
    }

    @Override
    public ChatRoom findByUserAndHostAndProperty(User user, User host, Property property) {
        return iChatRoomRepository.findByUserAndHostAndProperty(user, host, property);
    }

    @Override
    public ChatRoom findById(Long chatRoomId) {
        return iChatRoomRepository.findChatRoomById(chatRoomId);
    }

    @Override
    public List<ChatRoom> findByHostAndProperties(User host, List<Property> properties) {
        return iChatRoomRepository.findByHostAndPropertyIn(host, properties);
    }
}
