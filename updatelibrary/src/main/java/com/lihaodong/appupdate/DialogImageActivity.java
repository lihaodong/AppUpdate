package com.lihaodong.appupdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.lihaodong.appupdate.util.AppUtils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lihaodong on 2017/6/10.
 */

public class DialogImageActivity extends Activity{
    private TextView lhd_update_id_ok;
    private NumberProgressBar number_progress_bar;
    private LinearLayout ll_bottom;
    private TextView lhd_update_id_cancel;
    private TextView lhd_update_content;
    private TextView lhd_update_mobile_tip;
    private String apkUrl;
    private String apkDes;
    private int forcedUpdate;
    private WifiInfo wifiInfo = null;       //获得的Wifi信息
    private WifiManager wifiManager = null; //Wifi管理器
//    private ImageView wifi_image;           //信号图片显示
    private int level;                      //信号强度值
    private Timer timer;
    private MyTimerTask mTimerTask;
    /**
     * 使用Handler实现UI线程与Timer线程之间的信息传递,每5秒告诉UI线程获得wifiIntfo
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setWifiImage(msg.what, checkNetworkConnection());
        }

    };
    private String filePath;
    private String fileName;
    private boolean isDowning=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lhd_update_image_dialog);
        filePath = getPath(this)+ "/yhbj/";
        apkUrl = getIntent().getExtras().getString("apkUrl");
        fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        apkDes = getIntent().getExtras().getString("apkDes");
        forcedUpdate =  getIntent().getExtras().getInt("forcedUpdate");
        initView();
        setData();
        setOnClickListener();
        // =============初始化wifi管理器===========
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // =============使用定时器,每隔5秒获得一次信号强度值===========
        timer = new Timer();
        mTimerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(mTimerTask, 1000, 5000);

    }

    private void setOnClickListener() {
        lhd_update_id_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forcedUpdate == 0) {
//                    tv_title.setText("正在下载");
                    ll_bottom.setVisibility(View.GONE);
                    number_progress_bar.setVisibility(View.VISIBLE);
                    OkHttpClientManager.downloadAsyn(
                            apkUrl,
                            filePath,
                            new OkHttpClientManager.ResultCallback<Object>()
                            {
                                @Override
                                public void onError(Request request, Exception e) {
                                    isDowning=false;
//                                    tv_title.setText("下载出错");
                                    Toast.makeText(DialogImageActivity.this,"下载出错",Toast.LENGTH_SHORT).show();
                                    ll_bottom.setVisibility(View.VISIBLE);
                                    number_progress_bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onResponse(Object response)
                                {
                                    isDowning=false;
                                    DialogImageActivity.this.finish();
                                    installApk(new File(filePath+fileName));
                                }

                                @Override
                                public void onDownloading(Object progress) {
                                    isDowning=true;
                                    number_progress_bar.setProgress((Integer) progress);
                                }
                            });
                } else if (forcedUpdate == 1) {
//                    tv_title.setText("正在下载");
                    ll_bottom.setVisibility(View.GONE);
                    number_progress_bar.setVisibility(View.VISIBLE);
                    OkHttpClientManager.downloadAsyn(
                            apkUrl,
                            filePath,
                            new OkHttpClientManager.ResultCallback<Object>()
                            {
                                @Override
                                public void onError(Request request, Exception e) {
                                    isDowning=false;
//                                    tv_title.setText("下载出错");
                                    Toast.makeText(DialogImageActivity.this,"下载出错",Toast.LENGTH_SHORT).show();
                                    ll_bottom.setVisibility(View.VISIBLE);
                                    number_progress_bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onResponse(Object response)
                                {
                                    isDowning=false;
                                    DialogImageActivity.this.finish();
                                    installApk(new File(filePath+fileName));
                                }

                                @Override
                                public void onDownloading(Object progress) {
                                    isDowning=true;
                                    number_progress_bar.setProgress((Integer) progress);
                                }
                            });
                }

            }
        });
    }
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, AppUtils.getPackageNAME(getApplicationContext()) + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (this.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            this.startActivity(intent);
        }
    }
    private void initView() {
        lhd_update_id_ok = (TextView) findViewById(R.id.lhd_update_id_ok);
        lhd_update_id_cancel = (TextView) findViewById(R.id.lhd_update_id_cancel);
//        wifi_image = (ImageView) findViewById(R.id.lhd_update_wifi_indicator);
        lhd_update_content = (TextView) findViewById(R.id.lhd_update_content);
        lhd_update_mobile_tip = (TextView) findViewById(R.id.lhd_update_mobile_tip);
        number_progress_bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
    }

    private void setData() {
        switch (forcedUpdate) {
            case 1:
                lhd_update_id_ok.setText(getText(R.string.LHDQZAppUpdate));
                lhd_update_id_cancel.setText(getText(R.string.LHDAppUpdateExit));
                lhd_update_id_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        APPUpdateAgent.mInter.exitApp();
                    }
                });
                break;
            case 0:
                lhd_update_id_ok.setText(getText(R.string.LHDUpdateNow));
                lhd_update_id_cancel.setText(getText(R.string.LHDNotNow));
                lhd_update_id_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogImageActivity.this.finish();
                    }
                });
                break;
        }
        lhd_update_content.setText(Html.fromHtml(apkDes));
    }

    private void setWifiImage(int wifiFlag, int netFlag) {
        switch (wifiFlag) {
            // 如果收到正确的消息就获取WifiInfo，改变图片并显示信号强度
            case 1:
                setImage(R.drawable.connect_enable_wifi_animation_3, netFlag);
                break;
            case 2:
                setImage(R.drawable.connect_enable_wifi_animation_2, netFlag);
                break;
            case 3:
                setImage(R.drawable.connect_enable_wifi_animation_1, netFlag);
                break;
            case 4:
                setImage(R.drawable.connect_enable_wifi_animation_0, netFlag);
                break;
            case 5:
                setImage(R.drawable.jjdxm_update_wifi_disable, netFlag);
                break;
            default:
                setImage(R.drawable.jjdxm_update_wifi_disable, netFlag);
                break;
        }
    }

    private void setImage(int imageId, int netflag) {
        switch (netflag) {
            case 0:
                lhd_update_mobile_tip.setVisibility(View.GONE);
//                wifi_image.setImageResource(imageId);
                break;
            case 1:
                lhd_update_mobile_tip.setVisibility(View.VISIBLE);
//                wifi_image.setImageResource(R.drawable.connect_mobile);
                break;
            case -1:
                lhd_update_mobile_tip.setVisibility(View.GONE);
//                wifi_image.setImageResource(R.drawable.jjdxm_update_wifi_disable);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(forcedUpdate==1){
                Toast.makeText(DialogImageActivity.this, "请更新到最新版本！",
                        Toast.LENGTH_SHORT).show();
                return false;
            }else{
                if (!isDowning) {
                    return super.onKeyDown(keyCode, event);
                } else {
                    Toast.makeText(DialogImageActivity.this, "正在下载，请稍后！",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    /**
     * 获取wifi还是流量
     *
     * @return 0 wifi 1 琉璃那个 -1 没网
     */
    public int checkNetworkConnection() {
        ConnectivityManager connectMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && (info.getType() == ConnectivityManager.TYPE_WIFI)) {
            return 0;
        } else if (info != null && (info.getType() == ConnectivityManager.TYPE_MOBILE)) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 定时器
     */
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            wifiInfo = wifiManager.getConnectionInfo();
            //获得信号强度值
            level = wifiInfo.getRssi();
            //根据获得的信号强度发送信息
            if (level <= 0 && level >= -50) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            } else if (level < -50 && level >= -70) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            } else if (level < -70 && level >= -80) {
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            } else if (level < -80 && level >= -100) {
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁退出定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }
    public String getPath(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                return externalCacheDir.getAbsolutePath();
            } else {
                return context.getFilesDir().getAbsolutePath();
            }
        } else {
            return context.getFilesDir().getAbsolutePath();
        }
    }
}
