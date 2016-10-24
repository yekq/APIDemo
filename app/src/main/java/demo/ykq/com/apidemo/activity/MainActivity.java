package demo.ykq.com.apidemo.activity;

import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import demo.ykq.com.apidemo.R;
import demo.ykq.com.apidemo.listener.RequestListener;
import demo.ykq.com.apidemo.model.ResponseData;
import demo.ykq.com.apidemo.net.APIHelper;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tv_name)
    private AppCompatAutoCompleteTextView mTvName;
    @ViewInject(R.id.tv_pw)
    private AppCompatAutoCompleteTextView mTvPw;
    @ViewInject(R.id.btn_submit)
    private AppCompatButton mBtnSubmit;

    @Event(R.id.btn_submit)
    private void onSubmitClick(View v)
    {
        //测试
        HashMap<String,String> urlParam=new HashMap<>();
        urlParam.put("name",mTvName.getText().toString());
        urlParam.put("password",APIHelper.getMd5PassWord(mTvPw.getText().toString()));
        APIHelper.buildInstance().get("User/Login.php", urlParam, new TypeToken<ResponseData<String>>() {
        }, new RequestListener<ResponseData<String>>() {
            @Override
            public void onSuccess(ResponseData<String> result) {
                mBtnSubmit.setText(result.getMsg());
            }

            @Override
            public void onFail(ResponseData<String> result) {
                mBtnSubmit.setText(result.getMsg());
            }
        });
    }



}
