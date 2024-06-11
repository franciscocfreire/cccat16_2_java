package br.com.freire.uber.application;

import br.com.freire.uber.resource.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetAccount {
    private final Resource resource;

    public GetAccount(Resource resource) {
        this.resource = resource;
    }

    public Account getAccount(String accountId) {
        return resource.getAccountById(accountId).map(result -> {
                    Account account = new Account();
                    account.setAccountId(((UUID) result.get("account_id")).toString());
                    account.setName((String) result.get("name"));
                    account.setEmail((String) result.get("email"));
                    account.setCpf((String) result.get("cpf"));
                    account.setCarPlate((String) result.get("car_plate"));
                    account.setPassenger((Boolean) result.get("is_passenger"));
                    account.setDriver((Boolean) result.get("is_driver"));
                    return account;
                })
                .orElse(null);
    }
}
