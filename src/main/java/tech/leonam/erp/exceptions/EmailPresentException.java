package tech.leonam.erp.exceptions;

public class EmailPresentException extends RuntimeException {
    public EmailPresentException() {
        super("Already registered email");
    }
}
