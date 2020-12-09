package com.gd.basic.crud;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import lombok.Data;
import lombok.ToString;

/**
 * ajax请求响应信息模板
 *
 * @author ZhouHR
 */
@Data
@ToString
public class Message {

    private boolean success = true;
    private Object message;
    private String path;
    private Object data;
    public static final Message EXCEPTION = new Message(false, "操作失败！");
    public static final Message SUCCESS = new Message(true, "操作成功！");

    public Message(boolean success) {
        this.success = success;
    }

    public Message(boolean success, Object message) {
        this.success = success;
        this.message = handle(message);
    }

    public Message(boolean success, Object message, String path) {
        this.success = success;
        this.message = handle(message);
        this.path = path;
    }

    private Object handle(Object message) {
        if (message instanceof Errors) {
            return convert(((Errors) message));
        }

        return message;
    }

    private Map<String, String> convert(Errors errors) {
        HashMap<String, String> map = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            map.put(error.getField(), error.getDefaultMessage());
        }
        return map;
    }
}