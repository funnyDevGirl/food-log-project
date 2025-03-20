package ru.foodlog.exception;

public class MealNotFoundException extends ApplicationRuntimeException {

    public MealNotFoundException(String message) {
        super(message);
    }
}
