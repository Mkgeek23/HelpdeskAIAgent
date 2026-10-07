package pl.emkgeek.helpdeskagent.config;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpElicitation;
import org.springframework.stereotype.Component;
import pl.emkgeek.helpdeskagent.dto.NotificationChannel;
import pl.emkgeek.helpdeskagent.dto.UserNotification;

import java.util.Map;
import java.util.UUID;

@Component
public class ElicitRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(ElicitRequestHandler.class);
    private final boolean confirm = true;
    private final int affectedUsers = 1;

    private final NotificationChannel<UserNotification> notificationChannel;

    public ElicitRequestHandler(NotificationChannel<UserNotification> notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    @McpElicitation(clients = "helpdesk")
    public McpSchema.ElicitResult handleElicitRequest(
            McpSchema.ElicitFormRequest request
    ) {
        String notificationId = UUID.randomUUID().toString();

        var notification = new UserNotification(
                notificationId,
                request.message(),
                request.requestedSchema()
        );

        notificationChannel.emit(notification);

        log.info(
                "Elicitation DEMO: notificationId={}, confirm={}, affectedUsers={}",
                notificationId,
                confirm,
                affectedUsers
        );

        return new McpSchema.ElicitResult(
                McpSchema.ElicitResult.Action.ACCEPT,
                Map.of(
                        "confirm", confirm,
                        "affectedUsers", affectedUsers
                )
        );
    }
}
