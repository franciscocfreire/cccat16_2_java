package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@SpringBootApplication
@RestController
public class SignupApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SignupApplication.class, args);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        UUID id = UUID.randomUUID();
        String sql = "SELECT * FROM cccat16.account WHERE email = ?";
        var acc = jdbcTemplate.queryForList(sql, request.getEmail());
        if (!acc.isEmpty()) return ResponseEntity.unprocessableEntity().body(new SignupResponse(-4));
        if (!(Pattern.matches("[a-zA-Z]+ [a-zA-Z]+", request.getName()))) return ResponseEntity.unprocessableEntity().body(new SignupResponse(-3));
        if (!Pattern.matches("^(.+)@(.+)$", request.getEmail())) return ResponseEntity.unprocessableEntity().body(new SignupResponse(-2));
        if (!validateCpf(request.getCpf())) ResponseEntity.unprocessableEntity().body( new SignupResponse(-1));
        if (request.isDriver()  && !request.getCarPlate().isEmpty() && !Pattern.matches("[A-Z]{3}[0-9]{4}", request.getCarPlate())) ResponseEntity.unprocessableEntity().body( new SignupResponse(-5));
        jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                id, request.getName(), request.getEmail(), request.getCpf(), request.getCarPlate(), request.isPassenger(), request.isDriver());
        return ResponseEntity.ok().body(new SignupResponse(id.toString()));
    }

    @GetMapping("/accounts/{accountId}")
    public AccountResponse getAccount(@PathVariable String accountId){
        String sql = "SELECT * FROM cccat16.account WHERE account_id = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, accountId);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(((UUID) result.get("account_id")).toString());
        accountResponse.setName((String) result.get("name"));
        accountResponse.setEmail((String) result.get("email"));
        accountResponse.setCpf((String) result.get("cpf"));
        accountResponse.setCarPlate((String) result.get("car_plate"));
        accountResponse.setPassenger((Boolean) result.get("is_passenger"));
        accountResponse.setDriver((Boolean) result.get("is_driver"));

        return accountResponse;

    }

    private boolean validateCpf(String cpf) {
        return CpfValidator.validate(cpf);
    }

    public static class SignupResponse {
        private String accountId;
        private int errorCode;

        // Construtor padrão
        public SignupResponse() {}

        public SignupResponse(String accountId) {
            this.accountId = accountId;
        }

        public SignupResponse(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getAccountId() {
            return accountId;
        }

        public int getErrorCode() {
            return errorCode;
        }
    }

    public class AccountResponse {
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

    static class SignupRequest {
        private String name;
        private String email;
        private String cpf;
        private String carPlate;
        private boolean isPassenger;
        private boolean isDriver;

        // Getters and setters

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

        public void setPassenger(boolean passenger) {
            isPassenger = passenger;
        }

        public boolean isDriver() {
            return isDriver;
        }

        public void setDriver(boolean driver) {
            isDriver = driver;
        }
    }
}
