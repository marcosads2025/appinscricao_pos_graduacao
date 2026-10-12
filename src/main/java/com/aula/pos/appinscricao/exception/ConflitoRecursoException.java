package com.aula.pos.appinscricao.exception;

import org.springframework.http.HttpStatus;

public class ConflitoRecursoException extends ApiException {
    public ConflitoRecursoException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
