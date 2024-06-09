package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class Resource {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getAccountByEmail(String email) {
        String sql = "SELECT * FROM cccat16.account WHERE email = ?";
        return jdbcTemplate.queryForMap(sql, email);

    }

    public Map<String, Object> getAccountById(String accountId) {
        String sql = "SELECT * FROM cccat16.account WHERE account_id = ?";
        return jdbcTemplate.queryForMap(sql, accountId);

    }

    public String saveAccount(Application.Account account) {
        UUID accountId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                accountId , account.getName(), account.getEmail(), account.getCpf(), account.getCarPlate(), account.isPassenger(), account.isDriver());

        return accountId.toString();


    }
}
