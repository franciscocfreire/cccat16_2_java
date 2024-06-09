package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
public class Resource {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/signup")
    public ResponseEntity<Api.SignupResponse> signup(@RequestBody Api.SignupRequest request) {
        Api.SignupResponse signupResponse = new Api.SignupResponse(0);
        UUID id = UUID.randomUUID();
        String sql = "SELECT * FROM cccat16.account WHERE email = ?";
        var acc = jdbcTemplate.queryForList(sql, request.getEmail());
        if (!acc.isEmpty()) signupResponse = new Api.SignupResponse(-4);
        if (!(Pattern.matches("[a-zA-Z]+ [a-zA-Z]+", request.getName()))) signupResponse = new Api.SignupResponse(-3);
        if (!Pattern.matches("^(.+)@(.+)$", request.getEmail())) signupResponse = new Api.SignupResponse(-2);
        if (!validateCpf(request.getCpf())) signupResponse = new Api.SignupResponse(-1);
        if (request.isDriver() && !request.getCarPlate().isEmpty() && !Pattern.matches("[A-Z]{3}[0-9]{4}", request.getCarPlate()))
            signupResponse = new Api.SignupResponse(-5);
        jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                id, request.getName(), request.getEmail(), request.getCpf(), request.getCarPlate(), request.isPassenger(), request.isDriver());
        if (signupResponse.getErrorCode() != 0) return ResponseEntity.unprocessableEntity().body(signupResponse);
        signupResponse = new Api.SignupResponse(id.toString());
        return ResponseEntity.ok(signupResponse);
    }

    @GetMapping("/accounts/{accountId}")
    public Application.Account getAccount(@PathVariable String accountId) {
        String sql = "SELECT * FROM cccat16.account WHERE account_id = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, accountId);

        Application.Account accountResponse = new Application.Account();
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
}
