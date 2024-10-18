package com.example.md6projecthndn.controller.chat;

import com.example.md6projecthndn.model.dto.ChatRoomResponse;
import com.example.md6projecthndn.model.entity.property.Property;
import com.example.md6projecthndn.model.entity.user.ChatMessage;
import com.example.md6projecthndn.model.entity.user.ChatRoom;
import com.example.md6projecthndn.model.entity.user.User;
import com.example.md6projecthndn.service.chat.IChatMessageService;
import com.example.md6projecthndn.service.chat.IChatRoomService;
import com.example.md6projecthndn.service.property.property.IPropertyService;
import com.example.md6projecthndn.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final IChatMessageService chatMessageService;
    private final IChatRoomService chatRoomService;
    private final IUserService userService;
    private final IPropertyService propertyService;
    public ChatController(IChatMessageService chatMessageService,
                          IChatRoomService chatRoomService,
                          IUserService userService,
                          IPropertyService propertyService){
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @PostMapping("/openChatRoom")
    public ResponseEntity<?> openChatRoom(@RequestParam Long propertyId) {
        System.out.println(propertyId+ "+++++++++ propertyId");

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(currentUsername); // Người dùng ROLE_USER
        System.out.println("currentUsername:" + currentUsername +"+++++++++++++++");

        Property property = propertyService.findById(propertyId);

        User host = property.getOwner();  // Người dùng ROLE_HOST

        ChatRoom chatRoom = chatRoomService.findByUserAndHostAndProperty(user, host, property);

        if (chatRoom == null) {
            // Nếu chưa có ChatRoom, tạo mới
            chatRoom = new ChatRoom();
            chatRoom.setUser(user);
            chatRoom.setHost(host);
            chatRoom.setProperty(property);
            chatRoom = chatRoomService.save(chatRoom);
        }

        List<ChatMessage> chatMessages = chatMessageService.getChatHistory(chatRoom);
        chatMessages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt())); // Sắp xếp theo thời gian gửi
        System.out.println(chatRoom.getId()+ "+++++++++++++++");

        // Tạo response bao gồm thông tin phòng chat và tin nhắn
        ChatRoomResponse response = new ChatRoomResponse(chatRoom, chatMessages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@RequestParam Long chatRoomId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(currentUsername);
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);

        if (chatRoom == null) {
            return ResponseEntity.notFound().build();
        }

        if (!chatRoom.getUser().equals(user) && !chatRoom.getHost().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Trả về lỗi 403 nếu không có quyền
        }

        List<ChatMessage> chatMessages = chatMessageService.getChatHistory(chatRoom);

        chatMessages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()));

        return ResponseEntity.ok(chatMessages);

    }

    @PostMapping("/sendMessage")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage chatMessage) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(currentUsername);
        ChatMessage savedMessage = chatMessageService.saveChatMessage(chatMessage);
        return ResponseEntity.ok(savedMessage);
    }
}
