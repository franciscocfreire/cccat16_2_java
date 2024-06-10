package br.com.freire.uber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationTest {
    @Autowired
    Application application;

    @Test
    @DisplayName("Deve criar uma conta para o passageiro")
    void deveCriarContaParaPassageiro() {
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        Api.SignupResponse responseSignup = application.signup(request);

        assertNotNull(responseSignup);
        assertNotNull(responseSignup.getAccountId());

        Application.Account account = application.getAccount(responseSignup.getAccountId());
        assertEquals(expectedName, account.getName());
        assertEquals(expectedEmail, account.getEmail());
        assertEquals(expectedCpf, account.getCpf());
    }

    @Test
    @DisplayName("Não deve criar uma conta para o passageiro")
    void naoDeveCriarContaParaPassageiro() {

        int expectedError = -3;
        String expectedName = "John";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        ValidationError validationError = assertThrows(ValidationError.class, () -> {
            application.signup(request);
        });

        assertEquals(expectedError, validationError.getErrorCode());
    }

    @Test
    @DisplayName("Deve criar uma conta para o motorista")
    void deveCriarContaParaMotorista() {
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";
        String expectedCarPlate = "DBG9456";


        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(false);
        request.setDriver(true);
        request.setCarPlate(expectedCarPlate);

        Api.SignupResponse responseSignup = application.signup(request);

        assertNotNull(responseSignup);
        assertNotNull(responseSignup.getAccountId());

        Application.Account account = application.getAccount(responseSignup.getAccountId());
        assertEquals(expectedName, account.getName());
        assertEquals(expectedEmail, account.getEmail());
        assertEquals(expectedCpf, account.getCpf());
    }
}
