package pl.emkgeek.helpdeskmcpserver.prompt;

import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpPrompt;

import java.util.List;

public class TicketPrompts {

    private final String triageTicketMessage;

    public TicketPrompts(String triageTicketMessage) {
        this.triageTicketMessage = triageTicketMessage;
    }

    @McpPrompt(name = "triage-ticket", description = "Analyze the ticket and determine its category and priority.")
    public McpSchema.GetPromptResult getTriageTicketPrompt(@McpArg(name = "description", description = "Ticket description", required = true) String description) {
        return McpSchema.GetPromptResult.builder(
                List.of(
                        buildPromptMessage(McpSchema.Role.USER, triageTicketMessage),
                        buildPromptMessage(McpSchema.Role.USER, "Opis zgłoszenia do analizy:\n" + description.strip())
                )
        ).build();
    }

    private McpSchema.PromptMessage buildPromptMessage(McpSchema.Role role, String content) {
        return McpSchema.PromptMessage.builder(role, McpSchema.TextContent.builder(content).build()).build();
    }
}
