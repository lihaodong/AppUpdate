package com.lihaodong.appupdate;

import android.content.Context;

/**
 * Created by lihaodong on 2017/8/23.
 */

public class APPUpdate {
    private Context mContext;
    private String apkPath="";
    private String serverVersionName="";
    private boolean isForce = false; //是否强制更新
    private int localVersionCode = 0;
    private int serverVersionCode = 0;
    private String localVersionName="";
    private APPUpdate(Context context) {
        this.mContext=context;
    }
    public static APPUpdate initialize(Context context) {

        return new APPUpdate(context);
    }
    public APPUpdate apkPath(String apkPath){
        this.apkPath = apkPath;
        return this;
    }

    public APPUpdate serverVersionCode(int serverVersionCode){
        this.serverVersionCode = serverVersionCode;
        return this;
    }

    public APPUpdate serverVersionName(String  serverVersionName){
        this.serverVersionName = serverVersionName;
        return this;
    }

    public APPUpdate isForce(boolean  isForce){
        this.isForce = isForce;
        return this;
    }
    public Context getContext() {
        return mContext;
    }

}
