package ru.davydov.exception;

public class IncorrectAmountTransferException extends Exception{
    public IncorrectAmountTransferException(String message) {
        super(message);
    }
}
