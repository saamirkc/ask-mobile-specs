package com.openai.openai.Service;
import org.springframework.web.multipart.MultipartFile;
public interface ChatService {


    void addPdf(MultipartFile file, String userId);

    String retreiveDataFromVectorDB(String message, String userId);

}

