package com.lihaodong.appupdate.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lihaodong.appupdate.DialogActivity;

/**
 * Created by lihaodong on 2017/4/21.
 */

public class AppUtils {
    /**
     * 获得版本名称 market_versions
     *
     * @return
     */
    public static String getVersionNAME(Context context) {
        String versionname = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionname = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionname;
    }
    /**
     * 获得版本名称 market_versions
     *
     * @return
     */
    public static String getPackageNAME(Context context) {
        String packagename = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            packagename = info.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packagename;
    }
    /**
     * 获得版本名称versionCode
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 更新APK的Dialog
     *
     * @param titleName
     * @param message
     * @param forcedUpdate
     */
    public static void updateAPKDialog(final Context context, final String apkurl, int titleName, String message,int forcedUpdate) {
        Intent i = new Intent(context,DialogActivity.class);
        i.putExtra("apkUrl",apkurl);
        i.putExtra("apkDes",message);
        i.putExtra("forcedUpdate",forcedUpdate);
        context.startActivity(i);

    }

}
