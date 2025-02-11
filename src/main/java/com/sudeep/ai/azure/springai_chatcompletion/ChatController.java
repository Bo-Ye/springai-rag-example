package com.sudeep.ai.azure.springai_chatcompletion;

import java.util.List;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {
    private AzureOpenAiChatModel chatModel;

    public ChatController(AzureOpenAiChatModel chatModel) {
        this.chatModel = chatModel;
        
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
    
        String simpleChatResponse = chatModel.call(message);
        return "Response to: " + simpleChatResponse;
    }

    @GetMapping("/chatWithPrompt")
    public String chatWithPrompt(@RequestParam String message) {
        Message systemMessage = new SystemMessage("""
            You are a helpful assistant who answers questions about the Java language.
            Always answer with the information from the most recent release of Java.
            Do not answer questions about other programming languages or topics unrelated to Java.

            """);
        Message userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        ChatResponse chatResponse = chatModel.call(prompt);
        return "Response: " + chatResponse.getResult().getOutput().getText();
    }

}
