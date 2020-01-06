package util;

import java.io.Serializable;

public class Response implements Serializable{
    private int status;
    private String message;
    private Object data;

    public Response(int status, String message, Object data) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public Response(int status, String message) {
        this.message = message;
        this.status = status;
    }

    public Response(Object data) {
        this.data = data;
        this.status = 0;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
