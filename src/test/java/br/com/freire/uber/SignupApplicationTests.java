package br.com.freire.uber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupApplicationTests {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("Deve criar uma conta para o passageiro")
    void deveCriarContaParaPassageiro() {
        int expectedErrorCode = 0;
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        SignupApplication.SignupRequest request = new SignupApplication.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<SignupApplication.SignupRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SignupApplication.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, SignupApplication.SignupResponse.class);

        assertEquals(HttpStatus.OK, responseSignup.getStatusCode());
        assertNotNull(responseSignup.getBody());
        assertNotNull(responseSignup.getBody().getAccountId());

        assertEquals(expectedErrorCode, responseSignup.getBody().getErrorCode());

        String urlAccount = "http://localhost:" + port + "/accounts/" + responseSignup.getBody().getAccountId();
        ResponseEntity<Map<String, Object>> responseGetAccount = restTemplate.exchange(urlAccount, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        Map<String, Object> outputGetAccount = responseGetAccount.getBody();
        assertEquals(expectedName, outputGetAccount.get("name"));
        assertEquals(expectedEmail, outputGetAccount.get("email"));
        assertEquals(expectedCpf, outputGetAccount.get("cpf"));


    }
}
