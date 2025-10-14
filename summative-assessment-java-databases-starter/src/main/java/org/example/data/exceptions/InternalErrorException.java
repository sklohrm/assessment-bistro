package org.example.data.exceptions;

public class InternalErrorException extends Exception {
    public InternalErrorException(Exception e) {
        super(e);
        e.printStackTrace();
    }

    public InternalErrorException() {
        super(new Exception("Database error."));
    }
}
