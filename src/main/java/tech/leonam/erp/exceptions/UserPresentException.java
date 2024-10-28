package tech.leonam.erp.exceptions;

public class UserPresentException extends RuntimeException {
    public UserPresentException() {
        super("Already registered user");
    }
}
