package demo.ykq.com.apidemo.listener;

/**
 * 请求监听<br/>
 * Created on 2016/10/24
 *
 * @author yekangqi
 */

public interface RequestListener<T> {
    void onSuccess(T result);
    void onFail(T result);
}
