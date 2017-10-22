package com.lihaodong.appupdate.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lihaodong.appupdate.APPUpdateAgent;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APPUpdateAgent.forceUpdate(this,"http://api.yiboshi.com/api/app/getAppVersion?app=zklb&deviceType=1",new APPUpdateAgent.ExitInterface(){
            @Override
            protected void exitApp() {
                App.finishAllActivity();
            }

            @Override
            public void updateListener(int flag, String version, String updateMsg) {
                super.updateListener(flag, version, updateMsg);
                System.out.println("=====版本号："+version+"  更新日志："+updateMsg);
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APPUpdateAgent.forceUpdate(MainActivity.this,"http://api.yiboshi.com/api/app/getAppVersion?app=zklb&deviceType=1",new APPUpdateAgent.ExitInterface(){
                    @Override
                    protected void exitApp() {
                        App.finishAllActivity();
                    }

                    @Override
                    public void updateListener(int haveUpdate, String version, String updateMsg) {
                        super.updateListener(haveUpdate, version, updateMsg);
                        System.out.println("=====版本号："+version+"  更新日志："+updateMsg);
                    }
                });
            }
        });
    }
}
