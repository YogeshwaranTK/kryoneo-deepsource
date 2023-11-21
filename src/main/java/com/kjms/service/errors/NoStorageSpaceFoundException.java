package com.kjms.service.errors;

public class NoStorageSpaceFoundException extends RuntimeException{
    public NoStorageSpaceFoundException(){
        super("No Storage Space");
    }
}
