package org.knulikelion.challengers_backend.service.Exception;

public class ClubJoinNotFoundException extends RuntimeException {
    public ClubJoinNotFoundException() {
        super("Join request not found");
    }

    public ClubJoinNotFoundException(String message) {
        super(message);
    }
}
