package ru.iteranet.exceptions;

public class IDIsNotInteger extends RuntimeException {
    public IDIsNotInteger(Object id){
        super("Введенное значение ID " + id.toString() + " не является числовым");
    }
}
