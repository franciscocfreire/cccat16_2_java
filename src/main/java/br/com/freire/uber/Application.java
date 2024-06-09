package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class Application {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Api.SignupResponse signup(Api.SignupRequest input) {
        UUID id = UUID.randomUUID();
        String sql = "SELECT * FROM cccat16.account WHERE email = ?";
        var acc = jdbcTemplate.queryForList(sql, input.getEmail());
        if (!acc.isEmpty()) return new Api.SignupResponse(-4);
        if (!(Pattern.matches("[a-zA-Z]+ [a-zA-Z]+", input.getName()))) return new Api.SignupResponse(-3);
        if (!Pattern.matches("^(.+)@(.+)$", input.getEmail())) return new Api.SignupResponse(-2);
        if (!validateCpf(input.getCpf())) return new Api.SignupResponse(-1);
        if (input.isDriver() && !input.getCarPlate().isEmpty() && !Pattern.matches("[A-Z]{3}[0-9]{4}", input.getCarPlate()))
            return new Api.SignupResponse(-5);
        jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                id, input.getName(), input.getEmail(), input.getCpf(), input.getCarPlate(), input.isPassenger(), input.isDriver());
        return new Api.SignupResponse(id.toString());
    }

    public Account getAccount(String accountId) {
        String sql = "SELECT * FROM cccat16.account WHERE account_id = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, accountId);

        Account account = new Account();
        account.setAccountId(((UUID) result.get("account_id")).toString());
        account.setName((String) result.get("name"));
        account.setEmail((String) result.get("email"));
        account.setCpf((String) result.get("cpf"));
        account.setCarPlate((String) result.get("car_plate"));
        account.setPassenger((Boolean) result.get("is_passenger"));
        account.setDriver((Boolean) result.get("is_driver"));

        return account;

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
