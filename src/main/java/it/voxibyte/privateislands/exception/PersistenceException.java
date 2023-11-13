package it.voxibyte.privateislands.exception;

public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
