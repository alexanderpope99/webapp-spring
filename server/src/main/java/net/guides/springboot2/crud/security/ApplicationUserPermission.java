package net.guides.springboot2.crud.security;

public enum ApplicationUserPermission {
    PERSOANA_READ("persoana:read"), PERSOANA_WRITE("persoana:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
