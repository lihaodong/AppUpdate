package com.lihaodong.appupdate;

import android.content.Context;
import android.text.TextUtils;

import com.lihaodong.appupdate.util.AppUtils;


/**
 * Created by lihaodong on 2017/6/8.
 */

public class APPUpdateAgent {
    public static int UPDATE_ERROR = 0;
    public static int UPDATE_NO = 1;
    public static int UPDATE_YES = 2;

    static ExitInterface mInter;

    /**
     */
    public static void forceUpdate(final Context context, final AppUpdateBean bean, final ExitInterface exitInterface) {
        mInter = exitInterface;
        try {
            if (null != bean) {
                String versionName = bean.version;
                final String apkUrl = bean.url;
                final String updateMsg = bean.updateMsg;
                final int forcedUpdate = bean.forcedUpdate;
                String currentVersionName = AppUtils.getVersionNAME(context);
                int currentVersionCode = AppUtils.getVersionCode(context);
                if (!currentVersionName.equals(versionName) && !TextUtils.isEmpty(apkUrl)) {
                    exitInterface.updateListener(UPDATE_YES, versionName, updateMsg);
                    AppUtils.updateAPKDialog(context, apkUrl, R.string.app_name, updateMsg, forcedUpdate);
                } else {
                    exitInterface.updateListener(UPDATE_NO, versionName, updateMsg);
                }
            }
        } catch (Exception e) {
            exitInterface.updateListener(UPDATE_ERROR, "", "");
        }
    }

    public abstract static class ExitInterface {
        protected abstract void exitApp();

        public void updateListener(int flag, String version, String updateMsg) {
        }
    }
}
