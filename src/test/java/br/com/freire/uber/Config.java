package br.com.freire.uber;

import br.com.freire.uber.resource.AccountDAO;
import br.com.freire.uber.resource.AccountDAOMemory;
import br.com.freire.uber.resource.MailerGateway;
import br.com.freire.uber.resource.MailerGatewayMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MailerGateway mailerGateway(){
        return new MailerGatewayMemory();
    }

    @Bean
    public AccountDAO accountDAO(){
        return new AccountDAOMemory();
    }
}
