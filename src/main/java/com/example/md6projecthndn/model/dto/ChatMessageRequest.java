package com.example.md6projecthndn.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageRequest {
    private Long senderId;
    private Long chatRoomId;
    private String content;

}
