package demo.ykq.com.apidemo.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import demo.ykq.com.apidemo.listener.RequestListener;
import demo.ykq.com.apidemo.model.ResponseData;
import demo.ykq.com.apidemo.utils.AES;

/**
 * 接口请求<br/>
 * Created on 2016/10/24
 *
 * @author yekangqi
 */

public class APIHelper {
    public static final String API_HOST="http://192.168.6.245/StudyDemo/API/";
    public static final String TAG="APIHelper";
    private static final String PW_KEY="_/.,~sasfsd#ex214";
    private static final String VERSION="v1.1.0";
    private static final String KEY="@fdvkmd.!#$%#&*(";
    private APIHelper(){}
    public static APIHelper buildInstance()
    {
        return new APIHelper();
    }

    public static String getMd5PassWord(String pw)
    {
        return AES.MD5(PW_KEY+pw);
    }

    public  <T>  Callback.Cancelable get(String api, HashMap<String,String> urlParam, final TypeToken<ResponseData<T>> typeToken, final RequestListener<ResponseData<T>> listener)
    {
        Callback.Cancelable cancelable=null;
        try {
            JSONObject json=new JSONObject();
            for (Map.Entry<String,String> item:urlParam.entrySet()) {
                String key=item.getKey();
                String value=item.getValue();
                json.put(key,value);
            }
            long time=System.currentTimeMillis();
            String md5Data=VERSION+KEY+time;
            final String key= AES.MD5(md5Data).toUpperCase().substring(8,24);//16位的md5

            String urlJsonData=json.toString();
            String enc= AES.Encrypt(urlJsonData,key);

            RequestParams params=new RequestParams(API_HOST+api);
            params.addQueryStringParameter("t",time+"");
            params.addQueryStringParameter("v",VERSION);
            params.addQueryStringParameter("data",enc);
            cancelable=x.http().get(params, new Callback.CacheCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    //解密
                    ResponseData<T> data;
                    try {
                        try {
                            result=AES.Decrypt(result,key);
                            LogUtil.d("成功结果:"+result);
                        } catch (Exception e) {
                            LogUtil.d("解密失败0:"+e);
                        }
                        if (null==result)
                        {
                            data=new ResponseData<T>();
                            data.setCode(-1);
                            data.setMsg("参数有误0");

                            LogUtil.d("解密失败1");
                        }
                         data=new Gson().fromJson(result,typeToken.getType());
                    } catch (JsonSyntaxException e) {
                        try {
                            JSONObject object=new JSONObject(result);
                            data=new ResponseData<T>();
                            data.setCode(object.optInt("code",-1));
                            data.setMsg(object.optString("msg",e.toString()));

                            LogUtil.d("参数有误1:"+e);
                        } catch (JSONException jsonEx) {
                            data=new ResponseData<T>();
                            data.setCode(-1);
                            data.setMsg(jsonEx.toString());

                            LogUtil.d("参数有误2:"+jsonEx);
                        }
                    }
                    dealResult(data,listener);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ResponseData data=new ResponseData();
                    data.setCode(-1);
                    data.setMsg(null==ex?"未知错误":ex.toString());
                    dealResult(data,listener);
                    LogUtil.d("onError:"+ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    LogUtil.d(cex.toString());
                }

                @Override
                public void onFinished() {
                    LogUtil.e("onFinished");
                }

                @Override
                public boolean onCache(String result) {
                    LogUtil.e("onCache");
                    return false;
                }
            });
        } catch (Exception e) {
            LogUtil.d("其他错误:"+e);
        }
        return cancelable;
    }

    private <T> void dealResult(ResponseData<T> data,RequestListener<ResponseData<T>> listener)
    {
        if (null!=listener)
        {
            if (null!=data && data.getCode()>0)
            {
                listener.onSuccess(data);
            }else
            {
                listener.onFail(data);
            }
        }
    }
}
