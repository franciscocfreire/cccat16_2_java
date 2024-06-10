package br.com.freire.uber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class Resource {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Map<String, Object>> getAccountByEmail(String email) {

        String sql = "SELECT * FROM cccat16.account WHERE email = ?";
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, email);
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Map<String, Object>> getAccountById(String accountId) {
        String sql = "SELECT * FROM cccat16.account WHERE account_id = ?";

        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, accountId);
            return Optional.of(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String saveAccount(Application.Account account) {
        jdbcTemplate.update("INSERT INTO cccat16.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver) VALUES (?, ?, ?, ?, ?, ?, ?)",
                account.getAccountId(), account.getName(), account.getEmail(), account.getCpf(), account.getCarPlate(), account.isPassenger(), account.isDriver());

        return account.getAccountId();


    }
}
