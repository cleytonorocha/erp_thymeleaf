package tech.leonam.erp.exceptions;

public class ClienteNaoDeletado extends RuntimeException {
    public ClienteNaoDeletado(String message) {
        super(message);
    }
}
