package com.lihaodong.appupdate.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lihaodong.appupdate.APPUpdateAgent;
import com.lihaodong.appupdate.ExitInterface;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APPUpdateAgent.forceUpdate(this,"http://api.yiboshi.com/api/app/getAppVersion?app=zklb&deviceType=1",new ExitInterface(){
            @Override
            public void exitApp() {
                App.finishAllActivity();
            }
        });
    }
}
