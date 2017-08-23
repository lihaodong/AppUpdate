package com.lihaodong.appupdate;

import android.content.Context;

/**
 * Created by lihaodong on 2017/8/23.
 */

public class APPUpdate {
    public static Context mContext;
    private APPUpdate() {
    }
    public static void initialize(Context context) {
        mContext=context;
    }
    public static Context getContext() {
        return mContext;
    }

}
