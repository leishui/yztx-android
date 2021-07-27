package cn.entergx.yztx.msg;

public class SimpleMsg {
    private int status;
    private Object msg;

    public SimpleMsg() {
    }

    public SimpleMsg(int status, Object msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
