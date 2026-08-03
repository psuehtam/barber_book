package com.barberbook.backend.domain.user;

public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException() {
        super("Este e-mail já está cadastrado");
    }
}
