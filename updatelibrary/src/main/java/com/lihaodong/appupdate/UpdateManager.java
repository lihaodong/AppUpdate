package com.lihaodong.appupdate;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lihaodong on 2017/6/7.
 */

public class UpdateManager {
    private UpdateDownloadRequest request;
    private static UpdateManager manager;
    private ThreadPoolExecutor threadPoolExecutor;
    private UpdateManager(){
        threadPoolExecutor= (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }
    static {
        manager=new UpdateManager();
    }
    public static UpdateManager getInstance(){
        return manager;
    }
    public void startDownloads(String downloadUrl,String localPath,UpdateDownloadListener updateDownloadListener){
        if(request!=null){
            return;
        }
        checkLocalFilePath(localPath);
        //开始真正的去下载任务
        request=new UpdateDownloadRequest(downloadUrl,localPath,updateDownloadListener);
        Future<?> future=threadPoolExecutor.submit(request);
    }

    /**
     * 检查文件路径是否存在
     * @param path
     */
    private void checkLocalFilePath(String path) {
        File dir=new File(path.substring(0,path.lastIndexOf("/")+1));
        if(!dir.exists()){
            dir.mkdir();
        }
        File file=new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
