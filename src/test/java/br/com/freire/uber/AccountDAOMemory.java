package br.com.freire.uber;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class AccountDAOMemory implements AccountDAO {

    Map<String, Application.Account> mapAccountByAccountId;
    Map<String, Application.Account> mapAccountByAccountEmail;

    public AccountDAOMemory() {
        this.mapAccountByAccountId = new HashMap<>();
        this.mapAccountByAccountEmail = new HashMap<>();
    }

    @Override
    public Optional<Map<String, Object>> getAccountByEmail(String email) {
        return Optional.ofNullable(convertAccountToMap(this.mapAccountByAccountEmail.get(email)));
    }

    @Override
    public Optional<Map<String, Object>> getAccountById(String accountId) {
        return Optional.ofNullable(convertAccountToMap(this.mapAccountByAccountId.get(accountId)));
    }

    @Override
    public String saveAccount(Application.Account account) {
        this.mapAccountByAccountId.put(account.getAccountId(),account);
        this.mapAccountByAccountEmail.put(account.getEmail(),account);
        return account.getAccountId();
    }

    private Map<String, Object> convertAccountToMap(Application.Account account) {
        if (account == null) return null;
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("account_id", UUID.fromString(account.getAccountId()));
        accountMap.put("name", account.getName());
        accountMap.put("email", account.getEmail());
        accountMap.put("cpf", account.getCpf());
        accountMap.put("car_plate", account.getCarPlate());
        accountMap.put("is_passenger", account.isPassenger());
        accountMap.put("is_driver", account.isDriver());

        return accountMap;
    }
}
