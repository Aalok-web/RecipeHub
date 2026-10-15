package com.recipehub.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Domain model representing an authenticated RecipeHub user.
 * Implements Serializable for HTTP session persistence.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String email;
    private String passwordHash;
    private String bio = "";
    private String role = "USER";
    private Timestamp createdAt;

    public User() {}

    public User(long id, String name, String email, String passwordHash, String role, Timestamp createdAt) {
        this(id, name, email, passwordHash, "", role, createdAt);
    }

    public User(long id, String name, String email, String passwordHash, String bio, String role, Timestamp createdAt) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.email = email != null ? email.trim().toLowerCase() : null;
        this.passwordHash = passwordHash;
        this.bio = bio != null ? bio.trim() : "";
        this.role = role != null && !role.isBlank() ? role : "USER";
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name != null ? name.trim() : null; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email != null ? email.trim().toLowerCase() : null; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getBio() { return bio != null ? bio : ""; }
    public void setBio(String bio) { this.bio = bio != null ? bio.trim() : ""; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role != null && !role.isBlank() ? role : "USER"; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    /** Returns first initial for fallback user avatar badge */
    public String getInitial() {
        if (name != null && !name.isBlank()) {
            return name.trim().substring(0, 1).toUpperCase();
        }
        if (email != null && !email.isBlank()) {
            return email.trim().substring(0, 1).toUpperCase();
        }
        return "U";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (id != 0 && user.id != 0) {
            return id == user.id;
        }
        return email != null && email.equalsIgnoreCase(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email != null ? email.toLowerCase() : id);
    }

    @Override
    public String toString() {
        // Exclude passwordHash for security
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", bio='" + (bio != null && bio.length() > 20 ? bio.substring(0, 20) + "..." : bio) + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
