package br.com.freire.uber;

public class CpfValidator {

    public static boolean validate(String cpf) {
        if (cpf == null) {
            return false;
        }
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) {
            return false;
        }
        if (allDigitsEqual(cpf)) {
            return false;
        }
        int firstDigit = calculateDigit(cpf, 9);
        int secondDigit = calculateDigit(cpf, 10);
        String calculatedCheckDigits = "" + firstDigit + secondDigit;
        String actualCheckDigits = cpf.substring(9, 11);
        return calculatedCheckDigits.equals(actualCheckDigits);
    }

    private static boolean allDigitsEqual(String cpf) {
        char firstDigit = cpf.charAt(0);
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != firstDigit) {
                return false;
            }
        }
        return true;
    }

    private static int calculateDigit(String cpf, int length) {
        int sum = 0;
        int weight = length + 1;
        for (int i = 0; i < length; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
