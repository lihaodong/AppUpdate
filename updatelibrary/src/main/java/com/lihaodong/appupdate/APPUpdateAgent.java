package com.lihaodong.appupdate;

import android.content.Context;
import android.text.TextUtils;

import com.lihaodong.appupdate.util.AppUtils;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

/**
 * Created by lihaodong on 2017/6/8.
 */

public class APPUpdateAgent {
    public static int UPDATE_ERROR = 0;
    public static int UPDATE_NO = 1;
    public static int UPDATE_YES = 2;

    static ExitInterface mInter;

    /**
     * @param url app接口地址
     */
    public static void forceUpdate(final Context context, final String url, final ExitInterface exitInterface) {
        mInter = exitInterface;
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String u) {

                try {
                    if (!TextUtils.isEmpty(u)) {
                        JSONObject object;
                        object = new JSONObject(u);
                        String versionName = object.optString("version");
                        final String apkUrl = object.optString("url");
                        final String updateMsg = object.optString("updateMsg");
                        final int forcedUpdate = object.optInt("forcedUpdate");
                        String currentVersionName = AppUtils.getVersionNAME(context);
                        int currentVersionCode = AppUtils.getVersionCode(context);
                        if (!currentVersionName.equals(versionName) && !TextUtils.isEmpty(url)) {
                            exitInterface.updateListener(UPDATE_YES, versionName, updateMsg);
                            AppUtils.updateAPKDialog(context, apkUrl, R.string.app_name, updateMsg, forcedUpdate);
                        }else{
                            exitInterface.updateListener(UPDATE_NO, versionName, updateMsg);
                        }
                    }
                } catch (Exception e) {
                    exitInterface.updateListener(UPDATE_ERROR, "", "");
                }
            }

            @Override
            public void onDownloading(String progress) {

            }
        });
    }

    public abstract static class ExitInterface {
        protected abstract void exitApp();
        public void updateListener(int flag, String version, String updateMsg) {

        }
    }
}
