package pl.emkgeek.helpdeskmcpserver.resource;

import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;
import pl.emkgeek.helpdeskmcpserver.service.TicketService;

import java.util.List;

@Component
public class TicketResources {

    private final TicketService ticketService;

    public TicketResources(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @McpResource(uri = "helpdesk://kb/vpn", description = "VPN article")
    public McpSchema.ReadResourceResult getVpnArticle() {
        String vpnArticle = ticketService.getVpnArticle();

        return new McpSchema.ReadResourceResult(
                List.of(
                        new McpSchema.TextResourceContents(
                                "helpdesk://kb/vpn",
                                "text/markdown",
                                vpnArticle
                        )
                )
        );
    }

    @McpResource(uri = "helpdesk://tickets/{id}", description = "Get ticket by ID")
    public McpSchema.ReadResourceResult getTicketDetails(String id) {
        String ticketDetails = ticketService.getTicketDetails(id);

        return new McpSchema.ReadResourceResult(
                List.of(
                        new McpSchema.TextResourceContents(
                                "helpdesk://tickets/" + id,
                                "text/markdown",
                                ticketDetails
                        )
                )
        );
    }
}