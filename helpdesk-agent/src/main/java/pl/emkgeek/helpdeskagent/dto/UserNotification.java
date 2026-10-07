package pl.emkgeek.helpdeskagent.dto;

import java.util.Map;

public record UserNotification(String progressToken,
                               String message,
                               Map<String, Object> formSchema) implements NotificationEvent {
}
