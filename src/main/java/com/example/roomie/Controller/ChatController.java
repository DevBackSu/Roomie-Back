package com.example.roomie.Controller;

import com.example.roomie.SwaggerForm.ChatControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChatController implements ChatControllerDocs {

    @GetMapping("/chat/")  // 사용자가 참여 중인 채팅 리스트 반환 (채팅방 id / 방 이름 / 참여자 이름 / 참여자 나이)
    public Map<String, Object> chattingList() {
        Map<String, Object> chatList = new HashMap<>();
        return chatList;
    }
}
