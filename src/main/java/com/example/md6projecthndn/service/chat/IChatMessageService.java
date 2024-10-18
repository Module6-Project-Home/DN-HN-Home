package com.example.md6projecthndn.service.chat;

import com.example.md6projecthndn.model.entity.user.ChatMessage;
import com.example.md6projecthndn.model.entity.user.ChatRoom;

import java.util.List;

public interface IChatMessageService {
    List<ChatMessage> getChatHistory(ChatRoom chatRoom);

    ChatMessage saveChatMessage(ChatMessage chatMessage);
}
