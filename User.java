import java.util.Objects;

public abstract class User {
    public enum Role { ADMIN, CUSTOMER }

    private final long id;
    private final String username;
    private final String email;
    private final Role role;

    protected User(long id, String username, String email, Role role) {
        if (id <= 0) throw new IllegalArgumentException("id must be > 0");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email required");
        this.id = id;
        this.username = username.trim();
        this.email = email.trim();
        this.role = Objects.requireNonNull(role, "role");
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }

    @Override public String toString() {
        return role + "{" + "id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}
