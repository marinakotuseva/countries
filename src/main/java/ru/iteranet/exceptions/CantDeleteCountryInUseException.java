package ru.iteranet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class CantDeleteCountryInUseException extends RuntimeException {
    public CantDeleteCountryInUseException(String name){
        super("Невозможно удалить страну " + name + ", т.к. с ней связаны города");
    }
}
