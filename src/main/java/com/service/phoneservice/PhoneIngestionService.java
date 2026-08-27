package com.service.phoneservice;

import com.dto.BrandResponse;
import com.dto.ModelResponse;
import com.dto.PhoneEmbeddingTextBuilder;
import com.dto.PhoneSpecResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneIngestionService {

    private final RestClient restClient;
    private final VectorStore vectorStore;
    private final PhoneEmbeddingTextBuilder embeddingTextBuilder;

    @Value("${MOBILE_API_KEY}")
    private String key;

    private List<String> brandsToIngest;

    @PostConstruct
    void init() {
        brandsToIngest = callMobileBrands();
    }

    public void ingestAll() {
        for (String brand : brandsToIngest) {
            List<String> models = fetchModels(brand);
            log.info("Brand '{}' -> {} models found", brand, models.size());

            for (String model : models) {
                try {
                    var spec = fetchSpec(brand, model);
                    if (spec == null || spec.phoneDetails() == null) {
                        log.warn("No spec data for {} {}", brand, model);
                        continue;
                    }
                    Document doc = buildDocument(spec);
                    vectorStore.add(List.of(doc));
                } catch (Exception e) {
                    log.error("Failed to ingest {} {}: {}", brand, model, e.getMessage());
                }

                try {
                    Thread.sleep(6500); // stay under RapidAPI's rate limit
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private Document buildDocument(PhoneSpecResponse spec) {
        String content = embeddingTextBuilder.build(spec);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("brand", spec.phoneDetails().brandValue());
        metadata.put("model", spec.phoneDetails().modelValue());
        metadata.put("year", spec.phoneDetails().yearValue());

        return new Document(content, metadata);
    }

    public List<String> callMobileBrands() {
        var response = restClient.get()
                .uri("https://mobile-phone-specs-database.p.rapidapi.com/gsm/all-brands")
                .header("x-rapidapi-key", key)
                .header("x-rapidapi-host", "mobile-phone-specs-database.p.rapidapi.com")
                .retrieve()
                .body(BrandResponse[].class);

        return response == null ? List.of() :
                Arrays.stream(response)
                        .map(BrandResponse::brandValue)
                        .limit(2)
                        .toList();
    }

    private List<String> fetchModels(String brand) {
        var response = restClient.get()
                .uri("https://mobile-phone-specs-database.p.rapidapi.com/gsm/get-models-by-brandname/{brand}", brand)
                .header("x-rapidapi-key", key)
                .header("x-rapidapi-host", "mobile-phone-specs-database.p.rapidapi.com")
                .retrieve()
                .body(ModelResponse[].class);


        return response == null ? List.of() : Arrays.stream(response).map(ModelResponse::modelValue).limit(2).toList();
    }

    private PhoneSpecResponse fetchSpec(String brand, String model) {
        return restClient.get()
                .uri("https://mobile-phone-specs-database.p.rapidapi.com/gsm/get-specifications-by-brandname-modelname/{brand}/{model}", brand, model)
                .header("x-rapidapi-key", key)
                .header("x-rapidapi-host", "mobile-phone-specs-database.p.rapidapi.com")
                .retrieve()
                .body(PhoneSpecResponse.class);
    }
}