package pl.emkgeek.helpdeskmcpserver.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.emkgeek.helpdeskmcpserver.repository.TicketRepository;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class TicketService {

    private final TicketRepository ticketRepository;
    private final String vpnArticle;
    private final JsonMapper jsonMapper;

    public TicketService(TicketRepository ticketRepository,
                         @Value("classpath:kb/vpn.md") Resource resource, @Qualifier("jacksonJsonMapper") JsonMapper jsonMapper) throws IOException {
        this.ticketRepository = ticketRepository;

        try (InputStream input = resource.getInputStream()) {
            this.vpnArticle = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }
        this.jsonMapper = jsonMapper;
    }

    public String getTicketDetails(String id) {
        Objects.requireNonNull(id, "Ticket ID cannot be null");
        var ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found for ID: " + id));

        return jsonMapper.writeValueAsString(ticket);
    }

    public String getVpnArticle() {
        return vpnArticle;
    }
}
