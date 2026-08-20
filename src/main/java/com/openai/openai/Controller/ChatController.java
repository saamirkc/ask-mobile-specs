package com.openai.openai.Controller;

import com.openai.openai.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor

public class ChatController {

    private final ChatService chatService;




    @GetMapping("/query")
    public String advancedRag(@RequestParam String message, String userId){
        return this.chatService.retreiveDataFromVectorDB(message, userId);
    }







}
