package com.recipehub.model;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.sql.Timestamp;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        User user = new User(1L, "Chef Julia", "julia@example.com", "hash123", "Passionate home baker and pastry lover.", "ADMIN", now);

        assertEquals(1L, user.getId());
        assertEquals("Chef Julia", user.getName());
        assertEquals("julia@example.com", user.getEmail());
        assertEquals("hash123", user.getPasswordHash());
        assertEquals("ADMIN", user.getRole());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals("Passionate home baker and pastry lover.", user.getBio());
        assertEquals("C", user.getInitial());

        user.setName("Julia Child");
        assertEquals("Julia Child", user.getName());
        assertEquals("J", user.getInitial());
        user.setEmail("jchild@example.com");
        assertEquals("jchild@example.com", user.getEmail());
        user.setRole("USER");
        assertFalse(user.isAdmin());
        user.setBio("Updated culinary bio");
        assertEquals("Updated culinary bio", user.getBio());
    }

    @Test
    public void testEqualsAndHashCode() {
        User u1 = new User(10L, "Alice", "alice@example.com", "hash1", "USER", null);
        User u2 = new User(10L, "Alice Updated", "alice@example.com", "hash2", "USER", null);
        User u3 = new User(20L, "Bob", "bob@example.com", "hash3", "USER", null);

        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    public void testToStringDoesNotLeakPassword() {
        User user = new User(1L, "Secret Agent", "agent@recipehub.com", "SuperSecretHash$2a$12$", "USER", null);
        String str = user.toString();

        assertTrue(str.contains("Secret Agent"));
        assertTrue(str.contains("agent@recipehub.com"));
        assertFalse("toString must not contain password hash", str.contains("SuperSecretHash"));
    }

    @Test
    public void testUserSerialization() throws IOException, ClassNotFoundException {
        User user = new User(99L, "Marco Pierre", "marco@recipehub.com", "secretHash", "USER", new Timestamp(System.currentTimeMillis()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(user);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            User deserialized = (User) ois.readObject();
            assertEquals(user.getId(), deserialized.getId());
            assertEquals(user.getName(), deserialized.getName());
            assertEquals(user.getEmail(), deserialized.getEmail());
        }
    }

    @Test
    public void testBCryptPasswordHashingAndVerification() {
        String rawPassword = "SecretRecipe123!";
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
        assertTrue(BCrypt.checkpw(rawPassword, hash));
        assertFalse(BCrypt.checkpw("WrongPassword", hash));
    }
}
