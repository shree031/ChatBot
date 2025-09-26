package com.example.chatbot.service;

import com.example.chatbot.model.ChatMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final ChatClient chatClient;
    private final Deque<ChatMessage> chatHistory;
    private static final int MAX_HISTORY = 10;

    public ChatbotService(ChatClient chatClient) {
        this.chatClient = chatClient;
        this.chatHistory = new ArrayDeque<>(MAX_HISTORY);
    }

    public String getAnswer(String prompt) {
        String formattedPrompt = "Answer only in Markdown. Keep it concise and relevant to the question:\n\n" + prompt;

        addToHistory(new ChatMessage("user", formattedPrompt));

        List<Message> messages = chatHistory.stream().map(msg -> {
            switch (msg.getRole()) {
                case "user":
                    return new UserMessage(msg.getContent());
                case "assistant":
                    return new AssistantMessage(msg.getContent());
                case "system":
                    return new SystemMessage(msg.getContent());
                default:
                    throw new IllegalArgumentException("Unknown role: " + msg.getRole());
            }
        }).collect(Collectors.toList());

        String answer = chatClient.prompt().messages(messages).call().content();

        addToHistory(new ChatMessage("assistant", answer));

        return answer;
    }

    private void addToHistory(ChatMessage msg) {
        if (chatHistory.size() == MAX_HISTORY) {
            chatHistory.removeFirst();
        }
        chatHistory.addLast(msg);
    }

    public List<ChatMessage> getHistory() {
        return List.copyOf(chatHistory);
    }

    public void clearHistory() {
        chatHistory.clear();
    }
}
