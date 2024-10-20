package com.example.md6projecthndn.service.chat;

import com.example.md6projecthndn.model.chat.ChatMessage;
import com.example.md6projecthndn.model.chat.ChatRoom;
import com.example.md6projecthndn.repository.chat.IChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService implements IChatMessageService {

    @Autowired
    private IChatMessageRepository chatMessageRepository;


    @Override
    public List<ChatMessage> getChatHistory(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoomOrderBySentAtDesc(chatRoom);
    }

    @Override
    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
