package pl.emkgeek.helpdeskagent.dto;

import java.util.List;

public record TicketClassification(Category category,
                                   Priority priority,
                                   String summary,
                                   String reasoning,
                                   boolean needsClarification,
                                   List<String> questions) {
}
