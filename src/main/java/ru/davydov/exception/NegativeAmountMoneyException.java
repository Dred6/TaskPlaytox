package ru.davydov.exception;

public class NegativeAmountMoneyException extends Exception{
    public NegativeAmountMoneyException(String message) {
        super(message);
    }
}
