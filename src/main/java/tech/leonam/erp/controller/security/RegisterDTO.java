package tech.leonam.erp.controller.security;

import tech.leonam.erp.model.enums.UserRole;

public record RegisterDTO(String login, String name, String password, UserRole role) {
    public RegisterDTO(String login, String name ,String password){
        this(login, name, password, UserRole.USER);
    }
    public RegisterDTO(){
        this(null, null, null, UserRole.USER);
    }
}
