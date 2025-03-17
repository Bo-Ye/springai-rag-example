package com.sudeep.ai.azure.springai.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class RagService {
    private final VectorStore vectorStore;
    
    
    public RagService(VectorStore vectorStore) {  
        this.vectorStore = vectorStore;
    }

    public void ingestAndWritetoVectorStore() {
        Resource resource = new DefaultResourceLoader().getResource("classpath:terms.txt");
        TextReader textReader = new TextReader(resource);
        List<Document> documents0 = textReader.get();

		List<Document> documents = new TokenTextSplitter().apply(documents0);
        vectorStore.write(documents);
    }

}
