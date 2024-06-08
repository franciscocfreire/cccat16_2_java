package br.com.freire.uber;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupApplicationTests {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void deveCriarContaParaPassageiro() {
        int expectedErrorCode = 0;
        String url = "http://localhost:" + port + "/signup";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        SignupApplication.SignupRequest request = new SignupApplication.SignupRequest();
        request.setName("John Doe");
        request.setEmail("john.doe" + Math.random() + "@gmail.com");
        request.setCpf("87748248800");
        request.setPassenger(true);
        request.setDriver(false);

        HttpEntity<SignupApplication.SignupRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SignupApplication.Response> response = restTemplate.exchange(url, HttpMethod.POST, entity, SignupApplication.Response.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getAccountId());

        assertEquals(expectedErrorCode, response.getBody().getErrorCode());
    }
}
