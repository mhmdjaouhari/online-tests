package util;

import java.io.Serializable;

public class Request implements Serializable {
    private String action;
    private Object data;

    public Request(String action, Object data) {
        this.data = data;
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
