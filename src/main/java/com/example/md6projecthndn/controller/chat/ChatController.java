    package com.example.md6projecthndn.controller.chat;
    
    import com.example.md6projecthndn.model.dto.ChatMessageRequest;
    import com.example.md6projecthndn.model.dto.ChatRoomResponse;
    import com.example.md6projecthndn.model.entity.property.Property;
    import com.example.md6projecthndn.model.chat.ChatMessage;
    import com.example.md6projecthndn.model.chat.ChatRoom;
    import com.example.md6projecthndn.model.entity.user.User;
    import com.example.md6projecthndn.service.chat.IChatMessageService;
    import com.example.md6projecthndn.service.chat.IChatRoomService;
    import com.example.md6projecthndn.service.property.property.IPropertyService;
    import com.example.md6projecthndn.service.user.IUserService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;
    
    import java.util.List;
    import java.util.stream.Collectors;
    
    @RestController
    @RequestMapping("/api/chat")
    public class ChatController {
    
        private final IChatMessageService chatMessageService;
        private final IChatRoomService chatRoomService;
        private final IUserService userService;
        private final IPropertyService propertyService;
        private final SimpMessagingTemplate messagingTemplate;
    
        public ChatController(IChatMessageService chatMessageService,
                              IChatRoomService chatRoomService,
                              IUserService userService,
                              IPropertyService propertyService,
                              SimpMessagingTemplate messagingTemplate){
            this.chatMessageService = chatMessageService;
            this.chatRoomService = chatRoomService;
            this.userService = userService;
            this.propertyService = propertyService;
            this.messagingTemplate = messagingTemplate;
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
                chatRoom = new ChatRoom();
                chatRoom.setUser(user);
                chatRoom.setHost(host);
                chatRoom.setProperty(property);
                chatRoom = chatRoomService.save(chatRoom);
            } else {
                System.out.println("Chat room already exists with ID: " + chatRoom.getId());
            }
    
            List<ChatMessage> chatMessages = chatMessageService.getChatHistory(chatRoom);
            chatMessages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()));
            System.out.println(chatRoom.getId()+ "+++++++++++++++");
    
            ChatRoomResponse response = new ChatRoomResponse(chatRoom, chatMessages);
            return ResponseEntity.ok(response);
        }


        @GetMapping("/hostChatRoom")
        public ResponseEntity<?> hostChatRoom(@RequestParam Long chatRoomId) {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User host = userService.findByUsername(currentUsername);
    
            ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
            List<ChatMessage> chatMessages = chatMessageService.getChatHistory(chatRoom);
            chatMessages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()));
    
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
        public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User sender = userService.findByUsername(currentUsername);
    
            if (sender == null || !sender.getId().equals(chatMessageRequest.getSenderId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user credentials");
            }
    
            ChatRoom chatRoom = chatRoomService.findById(chatMessageRequest.getChatRoomId());
            if (chatRoom == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat room not found");
            }
    
            if (!chatRoom.getUser().equals(sender) && !chatRoom.getHost().equals(sender)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not part of this chat room");
            }
    
            ChatMessage newMessage = new ChatMessage();
            newMessage.setChatRoom(chatRoom);
            newMessage.setSender(sender);
            newMessage.setContent(chatMessageRequest.getContent());
    
            ChatMessage savedMessage = chatMessageService.saveChatMessage(newMessage);
            messagingTemplate.convertAndSend("/topic/notifications/" + chatRoom.getHost().getId(), savedMessage);
            return ResponseEntity.ok(savedMessage);
        }
    
        @GetMapping("/chatRooms")
        public ResponseEntity<List<ChatRoomResponse>> getChatRoomsForHost() {
            // Lấy host hiện tại từ SecurityContext
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            User host = userService.findByUsername(currentUsername);
    
            // Lấy danh sách các property của host
            List<Property> hostProperties = propertyService.findByOwner(host);
    
            // Lấy danh sách phòng chat tương ứng với các property đó
            List<ChatRoom> chatRooms = chatRoomService.findByHostAndProperties(host, hostProperties);
    
            List<ChatRoomResponse> response = chatRooms.stream()
                    .map(chatRoom -> {
                        List<ChatMessage> chatMessages = chatMessageService.getChatHistory(chatRoom);
                        chatMessages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()));
                        return new ChatRoomResponse(chatRoom, chatMessages);
                    })
                    .collect(Collectors.toList());
    
            return ResponseEntity.ok(response);
        }
    }
