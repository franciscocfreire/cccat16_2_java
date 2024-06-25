package br.com.freire.uber;

import br.com.freire.uber.application.Account;
import br.com.freire.uber.application.GetAccount;
import br.com.freire.uber.application.Signup;
import br.com.freire.uber.driver.SignupRequest;
import br.com.freire.uber.driver.SignupResponse;
import br.com.freire.uber.resource.MailerGateway;
import br.com.freire.uber.resource.Resource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension .class)
public class ApplicationMockTest {

    @Mock
    Resource resource;

    @Mock
    MailerGateway mailerGateway;

    @Test
    @DisplayName("Deve criar uma conta para o passageiro com stub")
    void deveCriarContaParaPassageiroComStub() {
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        SignupRequest request = new SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        Map<String, Object> mockResult = Map.of(
                "account_id", UUID.randomUUID(),
                "name", expectedName,
                "email", expectedEmail,
                "cpf", expectedCpf,
                "car_plate", "XYZ-1234",
                "is_passenger", true,
                "is_driver", false
        );

        when(resource.getAccountById(anyString())).thenReturn(Optional.of(mockResult));

        Signup signup = new Signup(resource, mailerGateway);

        ObjectMapper objectMapper = new ObjectMapper();
        SignupResponse responseSignup = signup.execute(objectMapper.convertValue(request, new TypeReference<>() {
        }));

        assertNotNull(responseSignup);
        assertNotNull(responseSignup.getAccountId());

        GetAccount getAccount = new GetAccount(resource);

        Account account = getAccount.getAccount(responseSignup.getAccountId());
        assertEquals(expectedName, account.getName());
        assertEquals(expectedEmail, account.getEmail());
        assertEquals(expectedCpf, account.getCpf());
    }
}
