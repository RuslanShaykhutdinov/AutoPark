package com.autoPark.AutoPark.utils;

import org.springframework.beans.factory.annotation.Value;

/**
 * Оболочка для отправки
 * @author HououinKyoma2000
 */
//@Component
public class RestError {
    @Value("0")
    private int error;
    @Value("")
    private String errMessage;
    @Value("null")
    private Object data;

    public RestError() {
    }

    public RestError(Object data) {
        this.data = data;
    }

    public RestError(int error, String errMessage) {
        this.error = error;
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
