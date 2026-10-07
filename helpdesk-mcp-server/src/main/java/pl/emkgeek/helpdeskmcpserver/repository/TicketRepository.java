package pl.emkgeek.helpdeskmcpserver.repository;

import org.springframework.stereotype.Repository;
import pl.emkgeek.helpdeskmcpserver.dto.Category;
import pl.emkgeek.helpdeskmcpserver.dto.Priority;
import pl.emkgeek.helpdeskmcpserver.dto.Status;
import pl.emkgeek.helpdeskmcpserver.dto.Ticket;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class TicketRepository {

    private final ConcurrentMap<String, Ticket> tickets =
            new ConcurrentHashMap<>();

    public TicketRepository() {
        save(new Ticket(
                "TICKET-001",
                "Laptop nie uruchamia się",
                "Po naciśnięciu przycisku zasilania ekran pozostaje czarny.",
                Priority.HIGH,
                Category.HARDWARE,
                Status.OPEN,
                Instant.parse("2026-10-01T08:00:00Z")
        ));

        save(new Ticket(
                "TICKET-002",
                "Błąd podczas uruchamiania aplikacji",
                "Aplikacja księgowa zamyka się tuż po zalogowaniu.",
                Priority.MEDIUM,
                Category.SOFTWARE,
                Status.IN_PROGRESS,
                Instant.parse("2026-10-01T09:15:00Z")
        ));

        save(new Ticket(
                "TICKET-003",
                "Brak dostępu do sieci w biurze",
                "Wszyscy użytkownicy na drugim piętrze utracili dostęp do sieci.",
                Priority.CRITICAL,
                Category.NETWORK,
                Status.OPEN,
                Instant.parse("2026-10-01T10:30:00Z")
        ));

        save(new Ticket(
                "TICKET-004",
                "Nadanie dostępu do projektu",
                "Nowy pracownik potrzebuje dostępu do projektu HelpDesk w Jira.",
                Priority.MEDIUM,
                Category.ACCESS,
                Status.OPEN,
                Instant.parse("2026-10-01T11:00:00Z")
        ));

        save(new Ticket(
                "TICKET-005",
                "Aktualizacja danych kontaktowych",
                "Zaktualizowano numer telefonu w firmowym katalogu.",
                Priority.LOW,
                Category.OTHER,
                Status.CLOSED,
                Instant.parse("2026-10-01T12:45:00Z")
        ));
    }

    public List<Ticket> findAll() {
        return tickets.values().stream()
                .sorted(Comparator.comparing(Ticket::createdAt)
                        .thenComparing(Ticket::id))
                .toList();
    }

    public Optional<Ticket> findById(String id) {
        Objects.requireNonNull(id, "Id nie może być null");
        return Optional.ofNullable(tickets.get(id));
    }

    /**
     * Dodaje zgłoszenie lub zastępuje istniejące o tym samym ID.
     */
    public Ticket save(Ticket ticket) {
        Objects.requireNonNull(ticket, "Ticket nie może być null");
        Objects.requireNonNull(ticket.id(), "Id nie może być null");
        Objects.requireNonNull(ticket.createdAt(), "CreatedAt nie może być null");

        tickets.put(ticket.id(), ticket);
        return ticket;
    }

}
