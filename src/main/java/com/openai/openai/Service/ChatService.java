package com.openai.openai.Service;
import org.springframework.web.multipart.MultipartFile;
public interface ChatService {


    void addPdf(MultipartFile file);

    String retreiveDataFromVectorDB(String message);

}

