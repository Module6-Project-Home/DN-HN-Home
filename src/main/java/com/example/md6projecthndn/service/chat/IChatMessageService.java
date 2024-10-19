package com.example.md6projecthndn.service.chat;

import com.example.md6projecthndn.model.chat.ChatMessage;
import com.example.md6projecthndn.model.chat.ChatRoom;

import java.util.List;

public interface IChatMessageService {
    List<ChatMessage> getChatHistory(ChatRoom chatRoom);

    ChatMessage saveChatMessage(ChatMessage chatMessage);
}
