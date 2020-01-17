package util;

import java.io.Serializable;

public class Request implements Serializable {
    private Action action;
    private Object data;
    private Role role;

    public Request(Action action, Object data, Role role) {
        this.data = data;
        this.action = action;
        this.role = role;
    }

    public Request(Action action, Role role) {
        this.data = null;
        this.action = action;
        this.role = role;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
