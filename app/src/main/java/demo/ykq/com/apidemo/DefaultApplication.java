package demo.ykq.com.apidemo;

import android.app.Application;

import org.xutils.x;

/**
 * 默认Application<br/>
 * Created on 2016/10/24
 *
 * @author yekangqi
 */

public class DefaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
