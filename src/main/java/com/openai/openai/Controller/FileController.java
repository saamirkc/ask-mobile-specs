package com.openai.openai.Controller;


import com.openai.openai.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class FileController {

    private final ChatService chatService;


//    add pdf document.

    @PostMapping("/add")
    public ResponseEntity<String> addPdfFile(@RequestParam("file") MultipartFile file){
        this.chatService.addPdf(file);
        return ResponseEntity.ok("File Uploaded successfully");
    }


}
