package br.com.freire.uber.resource;

import br.com.freire.uber.application.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AccountDAOMemory implements AccountDAO {

    Map<String, Account> mapAccountByAccountId;
    Map<String, Account> mapAccountByAccountEmail;

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
    public String saveAccount(Account account) {
        this.mapAccountByAccountId.put(account.getAccountId(), account);
        this.mapAccountByAccountEmail.put(account.getEmail(), account);
        return account.getAccountId();
    }

    private Map<String, Object> convertAccountToMap(Account account) {
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
