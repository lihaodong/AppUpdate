package com.lihaodong.appupdate;

import android.content.Context;
import android.text.TextUtils;

import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lihaodong on 2017/6/8.
 */

public class APPUpdateAgent {
    static ExitInterface mInter;
    /**
     * @param url app接口地址
     */
    public static void forceUpdate(final Context context, final String url, ExitInterface exitInterface){
        mInter=exitInterface;
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>()
        {
            @Override
            public void onError(Request request, Exception e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String u)
            {

                try {
                    JSONObject object;
                    object = new JSONObject(u);
                    String versionName = object.optString("version");
                    final String apkUrl = object.optString("url");
                    final String updateMsg = object.optString("updateMsg");
                    final int forcedUpdate = 1;
                    String currentVersionName=AppUtils.getVersionNAME(context);
                    int currentVersionCode=AppUtils.getVersionCode(context);
                    if(!currentVersionName.equals(versionName)){
                        if(!TextUtils.isEmpty(url)){
                            AppUtils.updateAPKDialog(context,apkUrl,R.string.app_name,updateMsg,forcedUpdate);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDownloading(String progress) {

            }
        });
    }


    public static void exitApp() {

    }
}
