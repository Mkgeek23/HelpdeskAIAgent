package pl.emkgeek.helpdeskmcpserver.config;

import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketConfigurationTests {

    @Test
    void loadsPromptFromResourceWithoutFilesystemPath() throws Exception {
        String rules = "Przeanalizuj zgłoszenie i określ priorytet.";
        var resource = new ByteArrayResource(rules.getBytes(StandardCharsets.UTF_8));

        var prompts = new TicketConfiguration().ticketPrompts(resource);
        var result = prompts.getTriageTicketPrompt("  Brak dostępu do konta  ");

        assertEquals(rules, ((McpSchema.TextContent) result.messages().getFirst().content()).text());
        assertEquals("Opis zgłoszenia do analizy:\nBrak dostępu do konta",
                ((McpSchema.TextContent) result.messages().getLast().content()).text());
    }
}
