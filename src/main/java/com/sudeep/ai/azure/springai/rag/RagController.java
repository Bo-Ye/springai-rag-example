package com.sudeep.ai.azure.springai.rag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {
    private final RagService ragService;
    private AzureOpenAiChatModel chatModel;
    private final VectorStore vectorStore;
    

    public RagController(RagService ragService, AzureOpenAiChatModel chatModel, VectorStore vectorStore) {
        this.ragService = ragService;
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @PostMapping("/ai/ingest")
    public ResponseEntity<String> ingestAndWritedocument() {
        ragService.ingestAndWritetoVectorStore();
        return ResponseEntity.ok("OK");
    }
    
    @GetMapping("/chatWithRAG")
    public String chatWithRAG(@RequestParam("message") String message) {
        String systemMessageString = """
            You are a helpful assistant who answers questions about the services of Denali Travel Reservation Company.
            
            * Identify customer queries related to Denali Travel Reservation services.
            * Refer to the TNC section to provide accurate answers. 
            * If the answer is found in the Terms & Conditions, respond clearly and accurately, quoting the relevant section or clause if necessary. 
            * If the information is not available in the Terms & Conditions, respond by directing the customer to send an email to the provided contact for further assistance.

            TNC:
            {tnc}
            """;
        
        Message userMessage = new UserMessage(message);

        List<Document> similarDocuments = vectorStore.similaritySearch(message);
        String tncString = similarDocuments.stream()
            .map(Document::getFormattedContent)
            .collect(Collectors.joining("\n"));
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessageString);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("tnc", tncString));

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        ChatResponse chatResponse = chatModel.call(prompt);
        return "Response: " + chatResponse.getResult().getOutput().getText();
    }

    @GetMapping("/chatWithRAGAdvisor")
    public String chatWithRAGAdvisor(@RequestParam("message") String message) {
        String systemMessageString = """
            You are a helpful assistant who answers questions about the services of Denali Travel Reservation Company.
            
            * Identify customer queries related to Denali Travel Reservation services.
            * Refer to the Terms & Conditions to provide accurate answers. 
            * If the answer is found in the Terms & Conditions, respond clearly and accurately, quoting the relevant section or clause if necessary. 
            * If the information is not available in the Terms & Conditions, respond by directing the customer to send an email to the provided contact for further assistance.

            """;
        
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessageString);


        ChatResponse chatResponse = ChatClient.builder(chatModel).build()
            .prompt(systemPromptTemplate.create())
            .advisors(new QuestionAnswerAdvisor(vectorStore))
            .user(message)
            .call()
            .chatResponse();

        return "Response: " + chatResponse.getResult().getOutput().getText();
    }

}
