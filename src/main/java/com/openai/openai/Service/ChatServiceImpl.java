package com.openai.openai.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Value("classpath:prompts/user-message.st")
    private Resource userMessage;
    @Value("classpath:prompts/system-message.st")
    private Resource systemMessage;

    private final ChatClient chatClient;


    private final VectorStore vectorStore;

    private final Reader reader;

    private final S3Service s3Service;



//
//    @Override
//    public void addPdf(MultipartFile file) {
//       List<Document> list = this.reader.readPDF(file);
//       vectorStore.add(list);
//
//
//    }

    @Override
    public void addPdf(MultipartFile file, String userId) {

        String s3Key =s3Service.uploadFile(file);

        List<Document> list = this.reader.readPdfFromS3(s3Key,userId,file.getOriginalFilename());
        vectorStore.add(list);

    }

    @Override
    public String retreiveDataFromVectorDB(String message, String userId) {


//      1.  preprocess query(pre-retrieval)
        var advisor = RetrievalAugmentationAdvisor.builder().
//            1.1   rewrite a user query to provide better results
        queryTransformers(RewriteQueryTransformer.builder().chatClientBuilder(chatClient.mutate().clone()).build(),

//               convert given query language to target language. for eg. if query is in Nepali language it has to be converted
//               to English which is our target language in this case.
        TranslationQueryTransformer.builder().chatClientBuilder(chatClient.mutate()).targetLanguage("english").build()).

//          1.2       generate multiple semantically different versions/perspectives of the user's query.
        queryExpander(MultiQueryExpander.builder().chatClientBuilder(chatClient.mutate().clone()).numberOfQueries(3).build()).

                //        2. retrieve related data from vector db

//       2.1   retrieve similar documents from db  for each query
        documentRetriever(VectorStoreDocumentRetriever.builder().
        vectorStore(vectorStore).
        topK(3).
        similarityThreshold(0.4).
        filterExpression(new FilterExpressionBuilder().
                eq("userId",userId).
                build()).
        build()).
//eq("userId","userId")  first userId is the metadata that we save while reading pdf and second userId is our userId
//       2.2  combine documents
        documentJoiner(new ConcatenationDocumentJoiner()).

//    3.Post-retrieval:  context+query passed
        queryAugmenter(ContextualQueryAugmenter.builder().build()).

                build();


// call LLM
        return chatClient.
                prompt().
                advisors(advisor).
                user(message).
                call().
                content();
    }


}
