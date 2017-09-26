package com.cloud.exception;

public class RabbitException extends RuntimeException {

    public RabbitException (String msg){
        super(msg);
    }

    public RabbitException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    public RabbitException(Throwable throwable){
        super(throwable);
    }
}
