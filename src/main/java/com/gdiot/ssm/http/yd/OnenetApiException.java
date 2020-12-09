package com.gdiot.ssm.http.yd;

/**
 *
 * @author Yuzhou
 * @date 2017/3/21
 */
public class OnenetApiException extends RuntimeException {
    private String message = null;

    public String getMessage() {
        return message;
    }

    public OnenetApiException(String message) {
        this.message = message;
    }
}
