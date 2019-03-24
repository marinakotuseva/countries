package ru.iteranet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NotFoundException extends RuntimeException {
    public NotFoundException(){
        super("Не найдена запись");
    }
    public NotFoundException(long id){
        super("Не найдена запись с ID " + id);
    }
}
