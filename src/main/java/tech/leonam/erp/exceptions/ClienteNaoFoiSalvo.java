package tech.leonam.erp.exceptions;

public class ClienteNaoFoiSalvo extends RuntimeException {
    public ClienteNaoFoiSalvo(String message) {
        super(message);
    }
}
