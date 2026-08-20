package com.openai.openai.Controller;

import com.openai.openai.Service.ChatService;
import com.openai.openai.helper.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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
