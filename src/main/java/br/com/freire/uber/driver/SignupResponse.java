package br.com.freire.uber.driver;

public class SignupResponse {
    private String accountId;
    private int errorCode;

    // Construtor padrão
    public SignupResponse() {
    }

    public SignupResponse(String accountId) {
        this.accountId = accountId;
    }

    public SignupResponse(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public int getErrorCode() {
        return errorCode;
    }
}