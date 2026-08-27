    package com.config;

    import org.springframework.ai.chat.client.ChatClient;
    import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
    import org.springframework.ai.chat.memory.ChatMemory;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.client.RestClient;

    import java.util.List;
    @Configuration
    public class AiConfig {
        @Bean
        public ChatClient chatClient(ChatClient.Builder builder){

            return builder.
                    build();

        }

        @Bean
        public RestClient restClient(){
            return RestClient.builder()
                    .build();
        }

    }
