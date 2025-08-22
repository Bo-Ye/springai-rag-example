package com.sudeep.ai.azure.springai.toolcalling;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolCallingController {
    private OllamaChatModel chatModel;

    public ToolCallingController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/chatWithTool")
    public String chatWithTool(@RequestParam("message") String message) {
        String systemMessage = """
                    You are a helpful assistant who answers questions about Weather. 
                    Use your training data to provide answers about the questions. 
                    If the requested information is not avavilable in your training data, use the provided Tools to get the information.
                    The tool response is in JSON format. The forecasts for different time periods are available under the field named "periods". 
                    
                    If the requested information is not available from any sources, then respond by explaining the reason that information is not available. 

                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessage);
        ChatResponse chatResponse = ChatClient.builder(chatModel).build()
                .prompt(systemPromptTemplate.create())
                .tools(new WeatherTool())
                .user(message)
                .call()
                .chatResponse();

        return "Response: " + chatResponse.getResult().getOutput().getText();
    }
}
