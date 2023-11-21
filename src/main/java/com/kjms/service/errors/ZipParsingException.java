package com.kjms.service.errors;

public class ZipParsingException extends RuntimeException {
    public ZipParsingException(String reason, Exception inner) {
        super(reason, inner);
    }
}
