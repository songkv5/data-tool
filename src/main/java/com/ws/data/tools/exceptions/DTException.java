package com.ws.data.tools.exceptions;

/**
 * @author willis
 * @chapter
 * @section
 * @since 2018年10月21日 22:15
 */
public class DTException extends RuntimeException{
    private int code = 500;
    private String msg;

    public DTException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public DTException() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
