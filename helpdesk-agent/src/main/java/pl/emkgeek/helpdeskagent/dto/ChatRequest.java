package pl.emkgeek.helpdeskagent.dto;

public record ChatRequest(
        String conversationId,
        String message
) {
}
