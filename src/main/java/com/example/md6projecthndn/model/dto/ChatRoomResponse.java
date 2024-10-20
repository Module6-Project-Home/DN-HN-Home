package com.example.md6projecthndn.model.dto;

import com.example.md6projecthndn.model.chat.ChatMessage;
import com.example.md6projecthndn.model.chat.ChatRoom;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomResponse {
    private ChatRoom chatRoom;
    private List<ChatMessage> chatMessages;
    private User user;
    private Property property;

    public ChatRoomResponse(ChatRoom chatRoom, List<ChatMessage> chatMessages, User user, Property property) {
        this.chatRoom = chatRoom;
        this.chatMessages = chatMessages;
        this.user = user;
        this.property = property;
    }

    public ChatRoomResponse(ChatRoom chatRoom, List<ChatMessage> chatMessages) {
        this.chatRoom = chatRoom;
        this.chatMessages = chatMessages;
    }
}
