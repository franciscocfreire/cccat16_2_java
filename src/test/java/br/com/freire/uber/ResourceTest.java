package br.com.freire.uber;

import br.com.freire.uber.application.Account;
import br.com.freire.uber.resource.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ResourceTest {

    @Autowired
    Resource resource;

    @Test
    @DisplayName("Deve salvar um registro na tabela account e consultar por id")
    void deveSalvarUmRegistroNaTabelaAccountEConsultarPorId() {
        String expectedName = "John";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        Account account = new Account();
        account.setAccountId(UUID.randomUUID().toString());

        account.setName(expectedName);
        account.setEmail(expectedEmail);
        account.setCpf(expectedCpf);
        account.setPassenger(true);
        account.setDriver(false);

        String accountId = resource.saveAccount(account);

        Optional<Map<String, Object>> optionalSavedAccountById = resource.getAccountById(accountId);

        assertTrue(optionalSavedAccountById.isPresent(), "Esperado que a conta salva esteja presente");

        Map<String, Object> savedAccountById = optionalSavedAccountById.get();

        assertEquals(expectedName, savedAccountById.get("name"));
        assertEquals(expectedEmail, savedAccountById.get("email"));
        assertEquals(expectedCpf, savedAccountById.get("cpf"));
    }

    @Test
    @DisplayName("Deve salvar um registro na tabela account e consultar por id")
    void deveSalvarUmRegistroNaTabelaAccountEConsultarPorEmail() {
        String expectedName = "John";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        Account account = new Account();
        account.setAccountId(UUID.randomUUID().toString());
        account.setName(expectedName);
        account.setEmail(expectedEmail);
        account.setCpf(expectedCpf);
        account.setPassenger(true);
        account.setDriver(false);

        resource.saveAccount(account);

        Optional<Map<String, Object>> optionalSavedAccountByEmail = resource.getAccountByEmail(expectedEmail);

        assertTrue(optionalSavedAccountByEmail.isPresent(), "Esperado que a conta salva esteja presente");

        Map<String, Object> savedAccountByEmail = optionalSavedAccountByEmail.get();

        assertEquals(expectedName, savedAccountByEmail.get("name"));
        assertEquals(expectedEmail, savedAccountByEmail.get("email"));
        assertEquals(expectedCpf, savedAccountByEmail.get("cpf"));
    }


}