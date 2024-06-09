package br.com.freire.uber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Application.Account account = new Application.Account();
        account.setName(expectedName);
        account.setEmail(expectedEmail);
        account.setCpf(expectedCpf);
        account.setPassenger(true);
        account.setDriver(false);

        String accountId = resource.saveAccount(account);

        Map<String, Object> savedAccountById = resource.getAccountById(accountId);

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

        Application.Account account = new Application.Account();
        account.setName(expectedName);
        account.setEmail(expectedEmail);
        account.setCpf(expectedCpf);
        account.setPassenger(true);
        account.setDriver(false);

        resource.saveAccount(account);

        Map<String, Object> savedAccountByEmail = resource.getAccountByEmail(expectedEmail);

        assertEquals(expectedName, savedAccountByEmail.get("name"));
        assertEquals(expectedEmail, savedAccountByEmail.get("email"));
        assertEquals(expectedCpf, savedAccountByEmail.get("cpf"));
    }


}