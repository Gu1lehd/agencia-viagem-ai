package dev.ia.assistant;

import io.quarkiverse.langchain4j.RegisterAiService;

//Esta anotacao intrui o quarkus a gerar uma implementacao desta interface
@RegisterAiService
public interface TravelAgentAssistant {

    String chat (String userMessage);
}
