package br.com.freire.uber.resource;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MailerGatewayMemory implements MailerGateway{
    @Override
    public void send(String recipient, String subject, String content) {
        log.info("\nRecipient: {} \nSubject: {} \nContent: {}", recipient, subject, content);

    }
}
