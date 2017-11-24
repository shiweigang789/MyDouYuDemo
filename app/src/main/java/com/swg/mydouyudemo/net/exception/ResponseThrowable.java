package com.swg.mydouyudemo.net.exception;

/**
 * Created by swg on 2017/11/22.
 */

public class ResponseThrowable extends Exception {

    public int code;
    public String msg;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

}
