package br.com.freire.uber;

import java.util.Map;
import java.util.Optional;

public interface AccountDAO {
    Optional<Map<String, Object>> getAccountByEmail(String email);

    Optional<Map<String, Object>> getAccountById(String accountId);

    String saveAccount(Application.Account account);
}
