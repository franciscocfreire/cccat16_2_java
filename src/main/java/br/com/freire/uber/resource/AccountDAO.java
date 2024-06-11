package br.com.freire.uber.resource;

import br.com.freire.uber.application.Account;

import java.util.Map;
import java.util.Optional;

public interface AccountDAO {
    Optional<Map<String, Object>> getAccountByEmail(String email);

    Optional<Map<String, Object>> getAccountById(String accountId);

    String saveAccount(Account account);
}
