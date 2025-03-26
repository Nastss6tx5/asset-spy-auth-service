package asset.spy.auth.service.model;

public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public static Role getDefaultRole() {
        return Role.ROLE_USER;
    }
}
