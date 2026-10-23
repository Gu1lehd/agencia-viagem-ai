package dev.ia.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import jakarta.ws.rs.Produces;

public class CharMemoryConfig {

    @Produces
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }
}
