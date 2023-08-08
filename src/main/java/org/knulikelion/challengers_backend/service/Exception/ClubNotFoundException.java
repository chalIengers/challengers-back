package org.knulikelion.challengers_backend.service.Exception;

public class ClubNotFoundException extends RuntimeException {
    public ClubNotFoundException() {
        super("Club not found");
    }

    public ClubNotFoundException(String message) {
        super(message);
    }
}
