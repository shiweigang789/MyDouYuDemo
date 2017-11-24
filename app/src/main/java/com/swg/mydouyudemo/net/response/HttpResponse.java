package com.swg.mydouyudemo.net.response;

/**
 * 网络请求数据基类
 * Created by swg on 2017/11/23.
 */

public class HttpResponse<T> {

    private int errror;
    private T data;

    public int getErrror() {
        return errror;
    }

    public void setErrror(int errror) {
        this.errror = errror;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "errror=" + errror +
                ", data=" + data +
                '}';
    }

}
