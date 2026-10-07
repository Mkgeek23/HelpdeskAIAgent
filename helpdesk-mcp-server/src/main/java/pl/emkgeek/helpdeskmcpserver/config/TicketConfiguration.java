package pl.emkgeek.helpdeskmcpserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import pl.emkgeek.helpdeskmcpserver.prompt.TicketPrompts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class TicketConfiguration {

    @Bean
    public TicketPrompts ticketPrompts(@Value("classpath:${prompts-filepath}triage-ticket.txt") Resource resource) throws IOException {
        var triageTicketMessage = resource.getContentAsString(StandardCharsets.UTF_8);
        return new TicketPrompts(triageTicketMessage);
    }
}
