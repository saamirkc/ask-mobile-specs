package com.openai.openai.Service;

import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Reader {

    public List<Document> readPDF(MultipartFile file);


    List<Document> readPdfFromS3(String s3Key,String userId,String fileName);

}
