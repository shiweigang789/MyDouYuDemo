package com.swg.mydouyudemo.net.exception;

/**
 * Created by swg on 2017/11/22.
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
