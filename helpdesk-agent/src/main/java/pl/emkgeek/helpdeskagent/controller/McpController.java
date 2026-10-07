package pl.emkgeek.helpdeskagent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.emkgeek.helpdeskagent.dto.ChatRequest;
import pl.emkgeek.helpdeskagent.dto.TicketClassification;
import pl.emkgeek.helpdeskagent.service.TicketTriageService;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/mcp")
public class McpController {

    private final ChatClient client;
    private final TicketTriageService triageService;

    public McpController(ChatClient client, TicketTriageService triageService) {
        this.client = client;
        this.triageService = triageService;
    }

    @PostMapping("/stream")
    public Flux<String> chat(@RequestBody ChatRequest request) {
        return this.client.prompt(request.message())
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        request.conversationId()
                ))
                .toolContext(Map.of("conversationId", request.conversationId(),
                        "progressToken", UUID.randomUUID().toString()))
                .stream()
                .content();
    }

    @PostMapping
    public String callChat(@RequestBody ChatRequest request) {
        return this.client.prompt()
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        request.conversationId()
                ))
                .user(request.message())
                .toolContext(Map.of("progressToken", UUID.randomUUID().toString()))
                .call()
                .content();
    }

    @PostMapping("/triage")
    public TicketClassification triageTicket(@RequestBody ChatRequest request) {
        return triageService.classify(request.message());
    }
}
