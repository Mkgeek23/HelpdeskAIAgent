package pl.emkgeek.helpdeskmcpserver.dto;

import java.time.Instant;

public record Ticket(String id,
                     String title,
                     String description,
                     Priority priority,
                     Category category,
                     Status status,
                     Instant createdAt
) {
}
