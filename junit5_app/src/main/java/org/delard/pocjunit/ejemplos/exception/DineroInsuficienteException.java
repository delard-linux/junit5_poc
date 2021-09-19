package org.delard.pocjunit.ejemplos.exception;

public class DineroInsuficienteException extends RuntimeException{

    public static final String DINERO_INSUFICIENTE_MSG = "No hay dinero suficiente en la cuenta";

    public DineroInsuficienteException(String message) {
        super(message);
    }
}
