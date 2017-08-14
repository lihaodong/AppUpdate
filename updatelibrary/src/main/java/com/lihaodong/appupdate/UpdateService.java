package com.lihaodong.appupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;

import static com.lihaodong.appupdate.AppUtils.getPackageNAME;

/**
 * Created by lihaodong on 2017/6/7.
 */

public class UpdateService extends Service {
    private String apkURL;
    private String filePath;
    private NotificationManager notificationManager;
    private String fileName;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        filePath = getPath(this)+ "/yhbj/";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            notifyUser(getString(R.string.update_download_failed), 0);
            stopSelf();
        }
        apkURL = intent.getStringExtra("apkUrl");
        fileName = apkURL.substring(apkURL.lastIndexOf("/") + 1);
        notifyUser(getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startDownload() {
        OkHttpClientManager.downloadAsyn(
                apkURL,
                filePath,
                new OkHttpClientManager.ResultCallback<Object>()
                {
                    @Override
                    public void onError(Request request, Exception e) {
                        notifyUser(getString(R.string.update_download_failed), 0);
                    }

                    @Override
                    public void onResponse(Object response)
                    {
                        String cmd = "chmod 777 " + filePath+fileName;
                        try {
                            Runtime.getRuntime().exec(cmd);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       installApk(new File(filePath+fileName));
                        notifyUser(getString(R.string.update_download_finish), 100);
                        stopSelf();
                    }

                    @Override
                    public void onDownloading(Object progress) {
                        notifyUser(getString(R.string.update_download_processing), (Integer) progress);

                    }
                });
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, getPackageNAME(getApplicationContext())+".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (this.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            this.startActivity(intent);
        }
    }
    private void notifyUser(String des, int progress) {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentText(des)
                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setDefaults(Notification.DEFAULT_LIGHTS);// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
        if (progress > 0 && progress < 100) {
            mBuilder.setProgress(100, progress, false);
        } else {
            mBuilder.setProgress(0, 0, false);
        }
        notificationManager.notify(0, mBuilder.build());

    }

    private PendingIntent getContentIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, getPackageNAME(getApplicationContext())+".fileprovider", new File(filePath+fileName));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath+fileName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
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
