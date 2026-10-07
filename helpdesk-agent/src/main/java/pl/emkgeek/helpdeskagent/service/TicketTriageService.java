package pl.emkgeek.helpdeskagent.service;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import pl.emkgeek.helpdeskagent.dto.TicketClassification;

import java.util.List;
import java.util.Map;

@Service
public class TicketTriageService {

    private final ChatClient client;
    private final McpSyncClient mcpClient;

    public TicketTriageService(ChatModel chatModel, List<McpSyncClient> mcpClients) {
        this.client = ChatClient.create(chatModel);
        this.mcpClient = mcpClients.stream()
                .filter(candidate -> "helpdesk".equals(candidate.getClientInfo().title()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("MCP client 'helpdesk' is not configured"));
    }

    public TicketClassification classify(String description) {
        var result = mcpClient.getPrompt(McpSchema.GetPromptRequest.builder("triage-ticket")
                .arguments(Map.of("description", description))
                .build());
        var messages = result.messages().stream()
                .map(this::toMessage)
                .toList();

        return client.prompt(Prompt.builder().messages(messages).build())
                .call()
                .entity(TicketClassification.class);
    }

    private Message toMessage(McpSchema.PromptMessage message) {
        if (!(message.content() instanceof McpSchema.TextContent content)) {
            throw new IllegalStateException("Triage prompt must contain text messages");
        }
        return switch (message.role()) {
            case USER -> UserMessage.builder().text(content.text()).build();
            case ASSISTANT -> AssistantMessage.builder().content(content.text()).build();
        };
    }
}
