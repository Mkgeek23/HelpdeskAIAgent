package pl.emkgeek.helpdeskmcpserver.tools;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.stereotype.Component;
import pl.emkgeek.helpdeskmcpserver.dto.*;
import pl.emkgeek.helpdeskmcpserver.repository.TicketRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class TicketTools {

    private static final Logger log = LoggerFactory.getLogger(TicketTools.class);

    private final TicketRepository repository;

    public TicketTools(TicketRepository repository) {
        this.repository = repository;
    }

    @McpTool(
            name = "create_ticket",
            description = "Creates a new ticket with the given title, description, priority, and category.")
    public Ticket createTicket(
            McpSyncRequestContext context,
            @McpToolParam(description = "Short title", required = true) String title,
            @McpToolParam(description = "Detailed problem description", required = true) String description,
            @McpToolParam(description = "LOW, MEDIUM, HIGH or CRITICAL", required = true) Priority priority,
            @McpToolParam(description = "Problem category", required = true) Category category) {

        if (priority == Priority.CRITICAL) {
            var result = context.elicit(
                    spec -> spec.message("Enter a detailed description of the critical issue and any immediate steps taken."),
                    CreateCriticalConfirmation.class);

            log.info("Critical ticket confirmation result: {}", result);

            var confirmation = result.structuredContent();
            if (result.action() != McpSchema.ElicitResult.Action.ACCEPT
                    || confirmation == null
                    || !confirmation.confirm()
                    || confirmation.affectedUsers() <= 0) {
                priority = Priority.HIGH;
            }
        }

        Ticket ticket = new Ticket(
                "TICKET-" + UUID.randomUUID(),
                title,
                description,
                priority,
                category,
                Status.OPEN,
                Instant.now()
        );

        return repository.save(ticket);
    }

    @McpTool(
            name = "search_tickets",
            description = "Searches for tickets based on status and category. Both parameters are optional.")
    public List<Ticket> searchTickets(
            @McpToolParam(description = "Ticket status", required = false) Status status,
            @McpToolParam(description = "Ticket category", required = false) Category category
    ) {
        return repository.findAll().stream()
                .filter(ticket -> status == null || ticket.status() == status)
                .filter(ticket -> category == null || ticket.category() == category)
                .toList();
    }

    @McpTool(name = "get_ticket", description = "Retrieves a ticket by its ID.")
    public Ticket getTicket(
            @McpToolParam(description = "Ticket ID", required = true) String id) {
        Objects.requireNonNull(id, "Ticket ID cannot be null");
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    }

    @McpTool(name = "close_ticket", description = "Closes a ticket with a closure comment.")
    public Ticket closeTicket(
            @McpToolParam(description = "Ticket ID", required = true) String id,
            @McpToolParam(description = "Closure comment", required = true) String comment) {
        Ticket ticket = getTicket(id);
        Ticket closedTicket = new Ticket(
                ticket.id(),
                ticket.title(),
                ticket.description() + "\n\nClosure Comment: " + comment,
                ticket.priority(),
                ticket.category(),
                Status.CLOSED,
                ticket.createdAt()
        );
        return repository.save(closedTicket);
    }

    @McpTool(name = "generateWeeklyReport", description = "Generates a weekly report of tickets.")
    public String generateWeeklyReport(McpSyncRequestContext context) throws InterruptedException {
        context.progress(spec -> spec.message("0.2").progress(20));
        Thread.sleep(1000);
        context.progress(spec -> spec.message("0.4").progress(40));
        long totalTickets = repository.findAll().size();
        Thread.sleep(1000);
        context.progress(spec -> spec.message("0.6").progress(60));
        Thread.sleep(1000);
        long openTickets = repository.findAll().stream().filter(ticket -> ticket.status() == Status.OPEN).count();
        context.progress(spec -> spec.message("0.8").progress(80));
        Thread.sleep(1000);
        long closedTickets = repository.findAll().stream().filter(ticket -> ticket.status() == Status.CLOSED).count();
        context.progress(spec -> spec.message("1.0").progress(100));

        return String.format("Weekly Report:\nTotal Tickets: %d\nOpen Tickets: %d\nClosed Tickets: %d", totalTickets, openTickets, closedTickets);
    }

}
