package com.lihaodong.appupdate.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lihaodong.appupdate.APPUpdateAgent;
import com.lihaodong.appupdate.AppUpdateBean;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUpdateBean appUpdateBean=new AppUpdateBean();
        appUpdateBean.id=5;
        appUpdateBean.app="ybs";
        appUpdateBean.clientType=1;
        appUpdateBean.version="4.1.3";
        appUpdateBean.url="http://7xptn9.com1.z0.glb.clouddn.com/app/DoctorOfMedicine_ceshi_4.1.1.apk";
        appUpdateBean.updateMsg="<h5>更新内容：<h5/>1.我的考试与练习全新登场<br/>2.修改查询学分功能<br/>3.优化应用无网络提示情况<br/>";
        APPUpdateAgent.forceUpdate(this,appUpdateBean,new APPUpdateAgent.ExitInterface(){

            @Override
            protected void exitApp() {
                App.finishAllActivity();
            }

            @Override
            public void updateListener(int flag, String version, String updateMsg) {
                super.updateListener(flag, version, updateMsg);
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                APPUpdateAgent.forceUpdate(MainActivity.this,"http://api.yiboshi.com/api/app/getAppVersion?app=zklb&deviceType=1",new APPUpdateAgent.ExitInterface(){
//                    @Override
//                    protected void exitApp() {
//                        App.finishAllActivity();
//                    }
//
//                    @Override
//                    public void updateListener(int haveUpdate, String version, String updateMsg) {
//                        super.updateListener(haveUpdate, version, updateMsg);
//                        System.out.println("=====版本号："+version+"  更新日志："+updateMsg);
//                    }
//                });
            }
        });
    }
}
