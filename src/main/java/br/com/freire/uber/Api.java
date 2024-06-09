package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Api {

    @Autowired
    Application application;

    public static void main(String[] args) {
        SpringApplication.run(Api.class, args);
    }

    @PostMapping("/signup")
    public ResponseEntity<Api.SignupResponse> signup(@RequestBody SignupRequest request) {
        Api.SignupResponse signupResponse = application.signup(request);
        if (signupResponse.getErrorCode() != 0) return ResponseEntity.unprocessableEntity().body(signupResponse);
        return ResponseEntity.ok(signupResponse);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Application.Account> getAccount(@PathVariable String accountId) {
        Application.Account account = application.getAccount(accountId);
        if (account != null) return ResponseEntity.ok(account);
        return ResponseEntity.notFound().build();
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

    public static class SignupResponse {
        private String accountId;
        private int errorCode;

        // Construtor padrão
        public SignupResponse() {
        }

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
}
