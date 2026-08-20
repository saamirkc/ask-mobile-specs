package com.openai.openai;

import com.openai.openai.Service.ChatService;
import com.openai.openai.Service.Reader;
import com.openai.openai.helper.Helper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OpenaiApplicationTests {
	@Autowired
	private ChatService chatService;
	@Autowired
	private Reader reader;

	@Autowired
	private VectorStore vectorStore;



//@Test
//	public void extractPDF(){
//	System.out.println("Extracting pdf to document started");
//	List<Document> list =reader.readPDF();
//	list.forEach(item->{
//		System.out.println(item);
//	});
//	System.out.println("Read completed");
//
//
////	now add in vector db
//	vectorStore.add(list);
//	System.out.println("Pdf added in vector db successfully");
//
//}



}
