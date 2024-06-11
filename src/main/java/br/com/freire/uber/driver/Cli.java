package br.com.freire.uber.driver;

import br.com.freire.uber.application.Signup;
import br.com.freire.uber.resource.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args) throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> input = new HashMap<>();
        String step = "";
        String command;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/app");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        AccountDAO accountDAO = new AccountDAODatabase(jdbcTemplate);
        MailerGateway mailerGateway = new MailerGatewayMemory();
        Resource resource = new Resource(accountDAO);
        Signup signup = new Signup(resource, mailerGateway);

        while (true) {
            command = scanner.nextLine();

            if (command.startsWith("signup-passenger")) {
                System.out.print("passenger-name: ");
                step = "name";
                continue;
            }

            if ("name".equals(step)) {
                input.put(step, command);
                System.out.print("passenger-email: ");
                step = "email";
                continue;
            }

            if ("email".equals(step)) {
                input.put(step, command);
                System.out.print("passenger-cpf: ");
                step = "cpf";
                continue;
            }

            if ("cpf".equals(step)) {
                input.put(step, command);
                input.put("passenger", "true");

                SignupResponse output = signup.execute(input);
                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println(objectMapper.writeValueAsString(output));
                break;
            }
        }
    }
}
