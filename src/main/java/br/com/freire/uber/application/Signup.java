package br.com.freire.uber.application;

import br.com.freire.uber.driver.SignupRequest;
import br.com.freire.uber.driver.SignupResponse;
import br.com.freire.uber.resource.MailerGateway;
import br.com.freire.uber.resource.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class Signup {

    private final Resource resource;
    private final MailerGateway mailerGateway;
    private final ObjectMapper objectMapper;


    public Signup(Resource resource, MailerGateway mailerGateway) {
        this.resource = resource;
        this.mailerGateway = mailerGateway;
        this.objectMapper = new ObjectMapper();
    }

    public SignupResponse execute(Map<String, String> request) {
        SignupRequest input = objectMapper.convertValue(request, SignupRequest.class);
        UUID accountId = UUID.randomUUID();
        var existingAccount = resource.getAccountByEmail(input.getEmail());
        if (existingAccount.isPresent()) throw new ValidationError("Account already exist", -4);
        if (!(Pattern.matches("[a-zA-Z]+ [a-zA-Z]+", input.getName()))) throw new ValidationError("Invalid name", -3);
        if (!Pattern.matches("^(.+)@(.+)$", input.getEmail())) throw new ValidationError("Invalid email", -2);
        if (!validateCpf(input.getCpf())) throw new ValidationError("Invalid CPF", -1);
        if (input.isDriver() && !input.getCarPlate().isEmpty() && !Pattern.matches("[A-Z]{3}[0-9]{4}", input.getCarPlate()))
            throw new ValidationError("Invalid car plate", -5);
        resource.saveAccount(mapperInputToAccount(input, accountId.toString()));
        mailerGateway.send(input.getEmail(), "Welcome", "");
        return new SignupResponse(accountId.toString());
    }

    private Account mapperInputToAccount(SignupRequest input, String accountId) {
        Account account = new Account();
        account.setAccountId(accountId);
        account.setCpf(input.getCpf());
        account.setDriver(input.isDriver());
        account.setEmail(input.getEmail());
        account.setPassenger(input.isPassenger());
        account.setCarPlate(input.getCarPlate());
        account.setName(input.getName());
        return account;
    }

    private boolean validateCpf(String cpf) {
        return CpfValidator.validate(cpf);
    }
}
