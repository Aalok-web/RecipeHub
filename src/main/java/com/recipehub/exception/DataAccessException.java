package com.recipehub.exception;

/**
 * Unchecked root exception representing a failure in the persistence/DAO layer.
 * Decouples caller layers from vendor-specific java.sql.SQLException.
 */
public class DataAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
