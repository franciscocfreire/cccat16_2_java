package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
    public Response signup(@RequestBody SignupRequest request) {
        try {
            UUID id = UUID.randomUUID();

            String sql = "SELECT * FROM cccat16.account WHERE email = ?";
            var acc = jdbcTemplate.queryForList(sql, request.getEmail());

            if (acc.isEmpty()) {
                if (Pattern.matches("[a-zA-Z]+ [a-zA-Z]+", request.getName())) {
                    if (Pattern.matches("^(.+)@(.+)$", request.getEmail())) {
                        if (validateCpf(request.getCpf())) {
                            if (request.isDriver()) {
                                if (Pattern.matches("[A-Z]{3}[0-9]{4}", request.getCarPlate())) {
                                    jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                                            id, request.getName(), request.getEmail(), request.getCpf(), request.getCarPlate(), request.isPassenger(), request.isDriver());
                                    return new Response(id.toString());
                                } else {
                                    return new Response(-5);
                                }
                            } else {
                                jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                                        id, request.getName(), request.getEmail(), request.getCpf(), request.getCarPlate(), request.isPassenger(), request.isDriver());
                                return new Response(id.toString());
                            }
                        } else {
                            return new Response(-1);
                        }
                    } else {
                        return new Response(-2);
                    }
                } else {
                    return new Response(-3);
                }
            } else {
                return new Response(-4);
            }
        } finally {
            // No need to close connection explicitly, managed by Spring
        }
    }

    private boolean validateCpf(String cpf) {
        return CpfValidator.validate(cpf);
    }

    public static class Response {
        private String accountId;
        private int errorCode;

        // Construtor padrão
        public Response() {}

        public Response(String accountId) {
            this.accountId = accountId;
        }

        public Response(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getAccountId() {
            return accountId;
        }

        public int getErrorCode() {
            return errorCode;
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
