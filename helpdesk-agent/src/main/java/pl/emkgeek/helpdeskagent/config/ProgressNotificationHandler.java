package pl.emkgeek.helpdeskagent.config;

import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpProgress;
import org.springframework.stereotype.Component;

@Component
public class ProgressNotificationHandler {

    Logger log = LoggerFactory.getLogger(ProgressNotificationHandler.class);

    @McpProgress(clients = {"helpdesk"})
    public void handleProgressNotification(McpSchema.ProgressNotification notification) {
        log.info("Received progress notification: {}", notification.message());
        log.info("Progress: {}%", notification.progress());
        log.info("Notification progress token: {}", notification.progressToken());
    }
}
