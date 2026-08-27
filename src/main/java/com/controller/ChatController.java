package com.controller;

import com.service.rag.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor

public class ChatController {

    private final ChatService chatService;


    @PostMapping("/add")
    public void addMobileSpecsInDB(){
       this.chatService.addPhoneSpecsInVectorDB();
    }


    @GetMapping("/query")
    public String advancedRag(@RequestParam String message){
        return this.chatService.retreiveDataFromVectorDB(message);
    }










}
