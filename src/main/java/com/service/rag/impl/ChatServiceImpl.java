package com.service.rag.impl;

import com.service.phoneservice.PhoneIngestionService;
import com.service.rag.ChatService;
import com.tool.CurrencyConverterTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {


    private final ChatClient chatClient;


    private final VectorStore vectorStore;


    private final CurrencyConverterTool currencyConverterTool;

    private final PhoneIngestionService phoneIngestionService;


    @Override
    public void addPhoneSpecsInVectorDB() {
        this.phoneIngestionService.ingestAll();
    }


    @Override
    public String retreiveDataFromVectorDB(String message) {

////      1.       preprocess query(pre-retrieval)
        var advisor = RetrievalAugmentationAdvisor.builder().
////       1.1     rewrite a user query to provide better results
        queryTransformers(RewriteQueryTransformer.builder().chatClientBuilder(chatClient.mutate().clone()).build(),

////               convert given query language to target language. for eg. if query is in Nepali language it has to be converted
////               to English which is our target language in this case.
        TranslationQueryTransformer.builder().chatClientBuilder(chatClient.mutate()).targetLanguage("english").build()).

////       1.2    generate multiple semantically different versions/perspectives of the user's query.
        queryExpander(MultiQueryExpander.builder().chatClientBuilder(chatClient.mutate().clone()).numberOfQueries(3).build()).

////       2.      retrieve related data from vector db
////       2.1     retrieve similar documents from db  for each query

        documentRetriever(VectorStoreDocumentRetriever.builder().
        vectorStore(vectorStore).
        topK(3).
        similarityThreshold(0.4).
        build()).

////       2.2  combine documents
        documentJoiner(new ConcatenationDocumentJoiner()).

////    3.Post-retrieval:  context+query passed
        queryAugmenter(ContextualQueryAugmenter.builder().build()).

                build();


        //// call LLM
        return chatClient.
                prompt().
                advisors(advisor).
                tools(currencyConverterTool).
                user(message).
                call().
                content();
    }


}
