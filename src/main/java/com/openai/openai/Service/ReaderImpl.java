package com.openai.openai.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ReaderImpl implements Reader {

    private final S3Service s3Service;
    @Override
    public List<Document> readPDF(MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("uploaded-", ".pdf");

            file.transferTo(tempFile);


            PagePdfDocumentReader pagePdfDocumentReader=new PagePdfDocumentReader(new FileSystemResource(tempFile),
                    PdfDocumentReaderConfig.builder()
                            .withPageTopMargin(0)
                            .withPageExtractedTextFormatter(ExtractedTextFormatter.builder().
                                    withNumberOfTopTextLinesToDelete(0).
                                    build())
                            .withPagesPerDocument(1)
                            .build()
            );
            return pagePdfDocumentReader.read();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read pdf", e);
        }
    }

    @Override
    public List<Document> readPdfFromS3(String s3Key, String fileName) {


        try(InputStream is= s3Service.downloadFile(s3Key)){

             byte [] bytes= is.readAllBytes();
            ByteArrayResource resource= new ByteArrayResource(bytes){
                @Override
                public String getFilename(){
                    return fileName;
                }
            };


            PagePdfDocumentReader pagePdfDocumentReader=new PagePdfDocumentReader(resource,
                    PdfDocumentReaderConfig.builder()
                            .withPageTopMargin(0)
                            .withPageExtractedTextFormatter(ExtractedTextFormatter.builder().
                                    withNumberOfTopTextLinesToDelete(0).
                                    build())
                            .withPagesPerDocument(1)
                            .build()
            );

            return pagePdfDocumentReader.read();
//            List<Document> documents = pagePdfDocumentReader.read().stream()
//                    .map(doc -> doc.mutate()
//                            .metadata("userId", userId)
//                            .metadata("fileName", fileName)
//                            .metadata("s3Key", s3Key)
//                            .build())
//                    .toList();

//            return documents;
        }
        catch (IOException e){
            throw new RuntimeException("Failed to read pdf from storage",e);

        }
    }

}

