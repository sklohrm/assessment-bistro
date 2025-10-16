package org.example.data.exceptions;

public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(Exception e) {
        super(e);
        e.printStackTrace();
    }

    public RecordNotFoundException() {
        super(new Exception("Record not found."));
    }
}
