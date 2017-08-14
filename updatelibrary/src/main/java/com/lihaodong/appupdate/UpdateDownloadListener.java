package com.lihaodong.appupdate;

/**
 * Created by lihaodong on 2017/6/7.
 */

public interface UpdateDownloadListener {
    /**
     * 下载请求开始回调
     */
    public void onStarted();
    /**
     * 进度更新回调
     */
    public void onProgressChanged(int progress, String downloadUrl);
    public void onFinished(int completeSize, String downloadUrl);
    public void onFailure();
}
