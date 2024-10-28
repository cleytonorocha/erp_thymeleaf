package tech.leonam.erp.model.enums;

public enum UserRole {
    ADMIN(0, "ADMIN"),
    USER(1, "USER");

    private Integer cod;
    private String description;

    private UserRole(Integer cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public Integer getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

}
