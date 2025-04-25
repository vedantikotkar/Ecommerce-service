//package com.example.auth.config;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class LLMChatbot {
//    private static final String API_KEY = "AIzaSyAve6GvWkSgHIyrc_kSTo0EukZ68CtmRKY";
//    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
//    private static final String HISTORY_FILE = "chat_history.json";
//    private final List<ChatMessage> history = new ArrayList<>();
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public LLMChatbot() {
//        loadHistory();
//    }
//
//    public String getResponse(String userInput) {
//        history.add(new ChatMessage("user", userInput));
//        String response = callGeminiAPI(userInput);
//        history.add(new ChatMessage("assistant", response));
//        return response;
//    }
//
//    private String callGeminiAPI(String userInput) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            List<Map<String, Object>> conversation = new ArrayList<>();
//            for (ChatMessage msg : history) {
//                conversation.add(Map.of("role", msg.role, "parts", List.of(Map.of("text", msg.content))));
//            }
//
//            Map<String, Object> requestBody = Map.of(
//                    "contents", conversation,
//                    "generationConfig", Map.of("temperature", 0.7, "maxOutputTokens", 500)
//            );
//
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
//            ResponseEntity<Map> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, Map.class);
//
//            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
//            if (candidates != null && !candidates.isEmpty()) {
//                List<Map<String, Object>> parts = (List<Map<String, Object>>) candidates.get(0).get("content");
//                return (String) parts.get(0).get("text");
//            }
//            return "I couldn't generate a response.";
//        } catch (Exception e) {
//            return "There was an error communicating with the Gemini API.";
//        }
//    }
//
//    public void saveHistory() {
//        try {
//            objectMapper.writeValue(new File(HISTORY_FILE), history);
//            System.out.println("Chat history saved.");
//        } catch (IOException e) {
//            System.out.println("Error saving history: " + e.getMessage());
//        }
//    }
//
//    public void loadHistory() {
//        try {
//            File file = new File(HISTORY_FILE);
//            if (file.exists()) {
//                history.addAll(objectMapper.readValue(file, new TypeReference<>() {}));
//                System.out.println("Loaded chat history.");
//            }
//        } catch (IOException e) {
//            System.out.println("Error loading history: " + e.getMessage());
//        }
//    }
//
//    public void clearHistory() {
//        history.clear();
//        System.out.println("Chat history cleared.");
//    }
//
//    public static void main(String[] args) {
//        LLMChatbot bot = new LLMChatbot();
//        System.out.println("GeminiBot: Hello! I'm GeminiBot. Type 'exit' to quit.");
//
//        while (true) {
//            System.out.print("You: ");
//            String userInput = System.console().readLine();
//
//            if (userInput.equalsIgnoreCase("exit")) {
//                System.out.println("GeminiBot: Goodbye!");
//                bot.saveHistory();
//                break;
//            } else if (userInput.equalsIgnoreCase("save")) {
//                bot.saveHistory();
//            } else if (userInput.equalsIgnoreCase("clear")) {
//                bot.clearHistory();
//            } else {
//                System.out.println("GeminiBot: " + bot.getResponse(userInput));
//            }
//        }
//    }
//}
