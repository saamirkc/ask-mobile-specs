package com.openai.openai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder , ChatMemory chatMemory){

        MessageChatMemoryAdvisor messageChatMemoryAdvisor= MessageChatMemoryAdvisor.builder(chatMemory).build();  // wraps chatmemory into advisor.This advisor tells the AI:“Before responding, look at previous conversation messages too.”
        return builder.
                defaultAdvisors(messageChatMemoryAdvisor).
                build();

    }

}
