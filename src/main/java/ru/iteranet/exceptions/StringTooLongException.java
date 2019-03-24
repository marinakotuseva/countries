package ru.iteranet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class StringTooLongException extends RuntimeException {
    public StringTooLongException(String name, int max) {
        super("Введена слишком длинная строка: введено " + name.length() + " символов, максимально " + max + " символов  (вы ввели: "+ name + ")");
    }
}
