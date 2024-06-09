package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class Application {

    @Autowired
    private Resource resource;

    public Api.SignupResponse signup(Api.SignupRequest input) {
        UUID accountId = UUID.randomUUID();
        var existingAccount = resource.getAccountByEmail(input.getEmail());
        if (existingAccount.isPresent()) return new Api.SignupResponse(-4);
        if (!(Pattern.matches("[a-zA-Z]+ [a-zA-Z]+", input.getName()))) return new Api.SignupResponse(-3);
        if (!Pattern.matches("^(.+)@(.+)$", input.getEmail())) return new Api.SignupResponse(-2);
        if (!validateCpf(input.getCpf())) return new Api.SignupResponse(-1);
        if (input.isDriver() && !input.getCarPlate().isEmpty() && !Pattern.matches("[A-Z]{3}[0-9]{4}", input.getCarPlate()))
            return new Api.SignupResponse(-5);
        resource.saveAccount(mapperInputToAccount(input, accountId.toString()));
        return new Api.SignupResponse(accountId.toString());
    }

    private Account mapperInputToAccount(Api.SignupRequest input, String accountId) {
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

    public Account getAccount(String accountId) {
        return resource.getAccountById(accountId).map(result -> {
                    Account account = new Account();
                    account.setAccountId(((UUID) result.get("account_id")).toString());
                    account.setName((String) result.get("name"));
                    account.setEmail((String) result.get("email"));
                    account.setCpf((String) result.get("cpf"));
                    account.setCarPlate((String) result.get("car_plate"));
                    account.setPassenger((Boolean) result.get("is_passenger"));
                    account.setDriver((Boolean) result.get("is_driver"));
                    return account;
                })
                .orElse(null);
    }

    private boolean validateCpf(String cpf) {
        return CpfValidator.validate(cpf);
    }


    public static class Account {
        private String accountId;
        private String name;
        private String email;
        private String cpf;
        private String carPlate;
        private boolean isPassenger;
        private boolean isDriver;

        // Getters e Setters

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getCarPlate() {
            return carPlate;
        }

        public void setCarPlate(String carPlate) {
            this.carPlate = carPlate;
        }

        public boolean isPassenger() {
            return isPassenger;
        }

        public void setPassenger(boolean isPassenger) {
            this.isPassenger = isPassenger;
        }

        public boolean isDriver() {
            return isDriver;
        }

        public void setDriver(boolean isDriver) {
            this.isDriver = isDriver;
        }
    }


}
