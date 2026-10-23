
# Agencia Viagem AI

Assistente virtual da **Mundo Viagens**: uma API em Java que conversa com o cliente, consulta um catálogo de pacotes por busca semântica (RAG) e executa ações de reserva por ferramentas (function calling).

O cliente envia uma pergunta em texto. O modelo local (Ollama) responde com base no catálogo indexado no PostgreSQL (pgvector) e, quando a pergunta pede listagem ou cancelamento, chama métodos Java expostos como tools.

## O que o projeto faz

- Atende perguntas sobre pacotes a partir do documento `src/main/resources/rag/pacotes-viagem.md`.
- Mantém o histórico recente da conversa por usuário (janela de 20 mensagens, em memória).
- Lista pacotes por categoria (`ADVENTURE` ou `TREASURE`).
- Cancela uma reserva somente quando o nome enviado no header é o mesmo nome do titular da reserva.

O modelo é instruído a usar apenas o material fornecido. Se a resposta não estiver nos documentos, ele deve recusar em vez de inventar.

## Stack

| Peça | Versão / detalhe |
| --- | --- |
| Java | 17 |
| Quarkus | 3.40.1 |
| Gradle | 9.1.0 (wrapper) |
| LangChain4j | extensão Quarkus (`quarkus-langchain4j-ollama` e `quarkus-langchain4j-pgvector` 1.13.3) |
| LLM | Ollama, modelo de chat `gpt-oss:20b` |
| Embeddings | Ollama, modelo `nomic-embed-text` (768 dimensões) |
| Vetores | PostgreSQL com a extensão `pgvector`, tabela `travel_embeddings` |
| API | Jakarta REST, `POST /travel` |

Grupo Maven: `dev.ia`. Artefato: `agencia-viagem-ai` `1.0.0-SNAPSHOT`.

## Arquitetura

```text
Cliente
  │  POST /travel
  │  Header: X-User-Name
  │  Body: texto da pergunta
  ▼
TravelAgentResource
  │  valida usuário e pergunta
  │  grava o usuário no SecurityContext (ThreadLocal)
  ▼
PackageExpert  (@RegisterAiService)
  │  system prompt da Mundo Viagens
  │  memória da conversa (@MemoryId = nome do usuário)
  │
  ├── RAG: trechos do catálogo recuperados em travel_embeddings
  │
  └── BookingTools
        ├── listPackagesByCategory(Category)
        └── cancelBooking(long bookingId)
              └── BookingService (mapa em memória)


# agencia-viagem-ai

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/agencia-viagem-ai-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- LangChain4j Ollama ([guide](https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-ollama.html)): Provides the basic integration of Ollama with LangChain4j
- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly JAX-RS)

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
