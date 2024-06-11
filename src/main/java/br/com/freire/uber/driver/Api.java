package br.com.freire.uber.driver;

import br.com.freire.uber.application.Account;
import br.com.freire.uber.application.GetAccount;
import br.com.freire.uber.application.Signup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Api {

    @Autowired
    GetAccount getAccount;
    @Autowired
    Signup signup;

    public static void main(String[] args) {
        SpringApplication.run(Api.class, args);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            SignupResponse signupResponse = signup.signup(request);
            return ResponseEntity.ok(signupResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), -1);
            return ResponseEntity.unprocessableEntity().body(errorResponse);
        }
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable String accountId) {
        Account account = getAccount.getAccount(accountId);
        if (account != null) return ResponseEntity.ok(account);
        return ResponseEntity.notFound().build();
    }


}
