package tech.leonam.erp.exceptions;

public class ClienteNaoExiste extends RuntimeException {
    public ClienteNaoExiste(String message) {
        super(message);
    }
}
