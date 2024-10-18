package com.example.md6projecthndn.model.dto;

import com.example.md6projecthndn.model.entity.user.ChatMessage;
import com.example.md6projecthndn.model.entity.user.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomResponse {
    private ChatRoom chatRoom;
    private List<ChatMessage> chatMessages;

    public ChatRoomResponse(ChatRoom chatRoom, List<ChatMessage> chatMessages) {
        this.chatRoom = chatRoom;
        this.chatMessages = chatMessages;
    }
}
