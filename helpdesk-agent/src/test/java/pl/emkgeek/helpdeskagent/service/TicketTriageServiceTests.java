package pl.emkgeek.helpdeskagent.service;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.ChatOptions;
import pl.emkgeek.helpdeskagent.dto.Category;
import pl.emkgeek.helpdeskagent.dto.Priority;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketTriageServiceTests {

    @Test
    void usesHelpdeskPromptAndMapsClassificationIncludingAccessAndClarification() {
        var otherClient = mock(McpSyncClient.class);
        when(otherClient.getClientInfo()).thenReturn(
                McpSchema.Implementation.builder("other-client", "1.0").title("other").build());
        var mcpClient = mock(McpSyncClient.class);
        when(mcpClient.getClientInfo()).thenReturn(
                McpSchema.Implementation.builder("helpdesk-client", "1.0").title("helpdesk").build());
        String description = "Nie mogę zalogować się do konta";
        String rules = "Przeanalizuj zgłoszenie. Nie twórz ani nie modyfikuj zgłoszeń.";
        var promptRequest = McpSchema.GetPromptRequest.builder("triage-ticket")
                .arguments(Map.of("description", description)).build();
        when(mcpClient.getPrompt(promptRequest)).thenReturn(McpSchema.GetPromptResult.builder(List.of(
                McpSchema.PromptMessage.builder(McpSchema.Role.USER, McpSchema.TextContent.builder(rules).build()).build(),
                McpSchema.PromptMessage.builder(McpSchema.Role.USER, McpSchema.TextContent.builder(description).build()).build()
        )).build());
        var chatModel = mock(ChatModel.class);
        when(chatModel.getOptions()).thenReturn(ChatOptions.builder().build());
        when(chatModel.call(any(Prompt.class))).thenReturn(new ChatResponse(List.of(new Generation(
                AssistantMessage.builder().content("""
                        {
                          "category": "ACCESS",
                          "priority": "MEDIUM",
                          "summary": "Brak dostępu do konta",
                          "reasoning": "Problem z logowaniem; wpływ nieznany",
                          "needsClarification": true,
                          "questions": ["Czy problem dotyczy innych osób?"]
                        }
                        """).build()))));
        var service = new TicketTriageService(chatModel, List.of(otherClient, mcpClient));

        var result = service.classify(description);

        assertEquals(Category.ACCESS, result.category());
        assertEquals(Priority.MEDIUM, result.priority());
        assertEquals("Brak dostępu do konta", result.summary());
        assertEquals("Problem z logowaniem; wpływ nieznany", result.reasoning());
        assertTrue(result.needsClarification());
        assertEquals(List.of("Czy problem dotyczy innych osób?"), result.questions());
        verify(mcpClient).getPrompt(promptRequest);
        var prompt = ArgumentCaptor.forClass(Prompt.class);
        verify(chatModel).call(prompt.capture());
        var messages = prompt.getValue().getInstructions();
        assertEquals(2, messages.size());
        assertEquals(MessageType.USER, messages.getFirst().getMessageType());
        assertEquals(rules, messages.getFirst().getText());
        assertTrue(messages.getLast().getText().contains(description));
    }
}
