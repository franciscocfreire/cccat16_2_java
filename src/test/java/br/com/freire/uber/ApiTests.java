package br.com.freire.uber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTests {

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

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Api.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);

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

    @Test
    @DisplayName("Não Deve criar uma conta para o passageiro se o email for invalido")
    void naoDeveCriarContaParaPassageiroSeEmailInvalido() throws JsonProcessingException {
        String expectedError = "Invalid email";
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random();
        String expectedCpf = "87748248800";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Api.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);
            fail("Expected HttpClientErrorException.UnprocessableEntity");
        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
            ErrorResponse errorResponse = new ObjectMapper().readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
            assertNotNull(errorResponse);
            assertEquals(expectedError, errorResponse.getMessage());
        }
    }

    @Test
    @DisplayName("Não Deve criar uma conta para o passageiro se o nome for invalido")
    void naoDeveCriarContaParaPassageiroSeNomeInvalido() throws JsonProcessingException {
        String expectedError = "Invalid name";
        String expectedName = "John";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Api.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);
            fail("Expected HttpClientErrorException.UnprocessableEntity");
        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
            ErrorResponse errorResponse = new ObjectMapper().readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
            assertNotNull(errorResponse);
            assertEquals(expectedError, errorResponse.getMessage());
        }
    }

    @Test
    @DisplayName("Não Deve criar uma conta para o passageiro se o email já existir")
    void naoDeveCriarContaParaPassageiroSeEmailExistir() throws JsonProcessingException {
        String expectedError = "Account already exist";
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);
            restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);
            fail("Expected HttpClientErrorException.UnprocessableEntity");
        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
            ErrorResponse errorResponse = new ObjectMapper().readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
            assertNotNull(errorResponse);
            assertEquals(expectedError, errorResponse.getMessage());
        }
    }

    @Test
    @DisplayName("Não Deve criar uma conta para o passageiro se o cpf for invalido")
    void naoDeveCriarContaParaPassageiroSeCpfInvalido() throws JsonProcessingException {
        String expectedError = "Invalid CPF";
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248830";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Api.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);
            fail("Expected HttpClientErrorException.UnprocessableEntity");
        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
            ErrorResponse errorResponse = new ObjectMapper().readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
            assertNotNull(errorResponse);
            assertEquals(expectedError, errorResponse.getMessage());
        }
    }

    @Test
    @DisplayName("Não Deve criar uma conta para o motorista se a placa for invalida")
    void naoDeveCriarContaParaMotoristaSePlacaInvalida() throws JsonProcessingException {
        String expectedError = "Invalid car plate";
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";
        String expectedCarPlate = "AAA999";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(false);
        request.setDriver(true);
        request.setCarPlate(expectedCarPlate);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Api.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);
            fail("Expected HttpClientErrorException.UnprocessableEntity");
        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
            ErrorResponse errorResponse = new ObjectMapper().readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
            assertNotNull(errorResponse);
            assertEquals(expectedError, errorResponse.getMessage());
        }
    }

    @Test
    @DisplayName("Deve criar uma conta para o motorista")
    void deveCriarContaParaMotorista() {
        int expectedErrorCode = 0;
        String expectedName = "John Doe";
        String expectedEmail = "john.doe" + Math.random() + "@gmail.com";
        String expectedCpf = "87748248800";
        String expectedCarPlate = "DBG9456";

        String urlSignup = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Api.SignupRequest request = new Api.SignupRequest();
        request.setName(expectedName);
        request.setEmail(expectedEmail);
        request.setCpf(expectedCpf);
        request.setPassenger(false);
        request.setDriver(true);
        request.setCarPlate(expectedCarPlate);

        HttpEntity<Api.SignupRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Api.SignupResponse> responseSignup = restTemplate.exchange(urlSignup, HttpMethod.POST, entity, Api.SignupResponse.class);

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
        assertEquals(expectedCarPlate, outputGetAccount.get("carPlate"));
    }
}
