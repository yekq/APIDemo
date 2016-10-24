package demo.ykq.com.apidemo.model;

/**
 * 服务器返回数据<br/>
 * Created on 2016/10/24
 *
 * @author yekangqi
 */

public class ResponseData<T> {
    private int code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
