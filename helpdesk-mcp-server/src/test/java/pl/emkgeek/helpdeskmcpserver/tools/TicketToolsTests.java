package pl.emkgeek.helpdeskmcpserver.tools;

import io.modelcontextprotocol.spec.McpSchema.ElicitResult.Action;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.ai.mcp.annotation.context.StructuredElicitResult;
import pl.emkgeek.helpdeskmcpserver.dto.Category;
import pl.emkgeek.helpdeskmcpserver.dto.CreateCriticalConfirmation;
import pl.emkgeek.helpdeskmcpserver.dto.Priority;
import pl.emkgeek.helpdeskmcpserver.repository.TicketRepository;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class TicketToolsTests {

    @ParameterizedTest
    @MethodSource("criticalConfirmations")
    void createsTicketWithPriorityBasedOnConfirmation(Action action,
                                                     CreateCriticalConfirmation confirmation,
                                                     Priority expectedPriority) {
        var context = mock(McpSyncRequestContext.class);
        when(context.elicit(any(), eq(CreateCriticalConfirmation.class)))
                .thenReturn(new StructuredElicitResult<>(action, confirmation, null));
        var repository = new TicketRepository();
        var tools = new TicketTools(repository);

        var ticket = tools.createTicket(context, "VPN", "Brak połączenia", Priority.CRITICAL, Category.NETWORK);

        assertEquals(expectedPriority, ticket.priority());
        assertEquals(ticket, repository.findById(ticket.id()).orElseThrow());
    }

    static Stream<Arguments> criticalConfirmations() {
        return Stream.of(
                Arguments.of(Action.ACCEPT, new CreateCriticalConfirmation(true, 1), Priority.CRITICAL),
                Arguments.of(Action.ACCEPT, new CreateCriticalConfirmation(false, 1), Priority.HIGH),
                Arguments.of(Action.ACCEPT, new CreateCriticalConfirmation(true, 0), Priority.HIGH),
                Arguments.of(Action.ACCEPT, new CreateCriticalConfirmation(true, -1), Priority.HIGH),
                Arguments.of(Action.ACCEPT, null, Priority.HIGH),
                Arguments.of(Action.DECLINE, null, Priority.HIGH),
                Arguments.of(Action.CANCEL, null, Priority.HIGH),
                Arguments.of(Action.DECLINE, new CreateCriticalConfirmation(true, 1), Priority.HIGH)
        );
    }

    @Test
    void createsNonCriticalTicketWithoutElicitation() {
        var context = mock(McpSyncRequestContext.class);
        var tools = new TicketTools(new TicketRepository());

        var ticket = tools.createTicket(context, "VPN", "Brak połączenia", Priority.HIGH, Category.NETWORK);

        assertEquals(Priority.HIGH, ticket.priority());
        verifyNoInteractions(context);
    }
}
