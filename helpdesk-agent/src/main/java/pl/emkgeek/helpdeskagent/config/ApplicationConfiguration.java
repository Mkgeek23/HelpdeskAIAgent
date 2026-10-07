package pl.emkgeek.helpdeskagent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.emkgeek.helpdeskagent.advisor.TokenUsageAdvisor;
import pl.emkgeek.helpdeskagent.dto.NotificationChannel;
import pl.emkgeek.helpdeskagent.dto.UserNotification;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder,
                          ToolCallbackProvider mcpTools,
                          ChatMemory chatMemory) {
        return builder
                .defaultSystem("""
                        Jesteś asystentem helpdesku IT. Pomagasz użytkownikom zgłaszać problemy.
                        Zanim utworzysz ticket, upewnij się, że znasz problem.
                        Przy problemach z VPN najpierw zaproponuj artykuł z bazy wiedzy.
                        """)
                .defaultTools(mcpTools)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor(),
                        new TokenUsageAdvisor())
                .build();
    }

    @Bean
    public NotificationChannel<UserNotification> notificationChannel() {
        var sink = Sinks.many().multicast().<UserNotification>onBackpressureBuffer();
        var flux = sink.asFlux().cache(0);
        return new NotificationChannel<UserNotification>(sink, flux);
    }

}
