package dev.ia.api;

import dev.ia.assistant.PackageExpert;
import dev.ia.security.SecurityContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/travel")
public class TravelAgentResource {

    @Inject
    PackageExpert expert;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ask(String question, @HeaderParam("X-User-Name") String userName) {
        if (userName == null || userName.isBlank()) {
            throw new NotAuthorizedException("Usuário precisa estar autenticado!");
        }
        if (question == null || question.isBlank()) {
            throw new BadRequestException("A pergunta não pode estar vazia.");
        }

        try {
            SecurityContext.setCurrentUser(userName.trim());
            return expert.chat(userName.trim(), question);
        } finally {
            SecurityContext.clear();
        }
    }
}
