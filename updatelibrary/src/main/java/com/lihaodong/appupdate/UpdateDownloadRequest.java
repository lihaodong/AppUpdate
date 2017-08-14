package com.lihaodong.appupdate;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by lihaodong on 2017/6/7.
 */

public class UpdateDownloadRequest implements Runnable{
    private String downloadUrl;
    private String localFilePath;
    private UpdateDownloadListener downloadListener;
    private boolean isDownloading=false;
    private long currentLength;
    private DownloadResponseHandler downloadResponseHandler;
    public UpdateDownloadRequest(String downloadUrl,String localFilePath,UpdateDownloadListener downloadListener) {
        this.downloadListener=downloadListener;
        this.downloadUrl=downloadUrl;
        this.localFilePath=localFilePath;
        this.isDownloading=true;
        this.downloadResponseHandler=new DownloadResponseHandler();
    }
    //真正的去建立连接的方法
    private void makeRequest()throws IOException,InterruptedException{
        if(!Thread.currentThread().isInterrupted()){
            try {
                URL url=new URL(downloadUrl);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(50000);
                connection.setRequestProperty("Connection","Keep-Alive");
                connection.connect();//放到子线程
                currentLength=connection.getContentLength();
                if(!Thread.currentThread().isInterrupted()){
                    downloadResponseHandler.sendResponseMessage(connection.getInputStream());
                }
            }catch (IOException e){
                throw  e;
            }
        }
    }
    @Override
    public void run() {
        try {
            makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //格式化数据
    private String getTowPointFloatStr(float value){
        DecimalFormat fnum=new DecimalFormat("0.00");
        return fnum.format(value);

    }
    //包含了下载过程所有可能出现的异常情况
    public enum  FailureCode{
        UnknownHost,Socket,SocketTimeout,ConnectTimeout,IO,HttpResponse,JSON,Interrupted
    }
    public class DownloadResponseHandler{
        protected  static final int SUCCESS_MESSAGE=0;
        protected  static final int FAILURE_MESSAGE=1;
        protected  static final int START_MESSAGE=2;
        protected  static final int FINISH_MESSAGE=3;
        protected  static final int NETWORK_OFF=4;
        protected  static final int PROGRESS_CHANGED=5;
        private int mCompleteSize=0;
        private int progress=0;
        private Handler handler;
        public DownloadResponseHandler(){
            handler=new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    handleSelfMessage(msg);
                }
            };
        }
        protected void sendFinishMessage(){
            sendMessage(obtainMessage(FINISH_MESSAGE,null));
        }

        private void sendProgressChangedMessage(int progress){
            sendMessage(obtainMessage(PROGRESS_CHANGED,new Object[]{progress}));
        }
        protected void sendFailureMessage(FailureCode failureCode){
            sendMessage(obtainMessage(FAILURE_MESSAGE,new Object[]{failureCode}));
        }
        protected Message obtainMessage(int responseMessage,Object response) {
            Message msg=null;
            if(handler!=null){
                msg=handler.obtainMessage(responseMessage,response);
            }else{
                msg=Message.obtain();
                msg.what=responseMessage;
                msg.obj=response;
            }
            return msg;
        }

        protected void sendMessage(Message msg) {
            if(handler!=null){
                handler.sendMessage(msg);
            }else{
                handleSelfMessage(msg);
            }
        }

        protected void handleSelfMessage(Message msg) {
            Object[] response;
            switch (msg.what){
                case FAILURE_MESSAGE:
                    response= (Object[]) msg.obj;
                    handleFailureMessage((FailureCode)response[0]);
                    break;
                case PROGRESS_CHANGED:
                    response= (Object[]) msg.obj;
                    handleProgressChangedMessage(((Integer)response[0]).intValue());
                    break;
                case FINISH_MESSAGE:
                    onFinish();
                    break;
            }
        }

        protected void handleFailureMessage(FailureCode failureCode) {
            onFailure(failureCode);
        }
        protected void handleProgressChangedMessage(int progress) {
            downloadListener.onProgressChanged(progress,"");
        }
        //接口的回调
        public void onFailure(FailureCode failureCode) {
            downloadListener.onFailure();
        }


        public void onFinish(){
            downloadListener.onFinished(mCompleteSize,"");
        }
        //文件下载方法 会发送时间
        void sendResponseMessage(InputStream is){
            RandomAccessFile randomAccessFile=null;
            mCompleteSize=0;
            try{
                byte[] buffer=new byte[1024];
                int length=-1;
                int limit=0;
                randomAccessFile=new RandomAccessFile(localFilePath,"rwd");
                while ((length=is.read(buffer))!=-1){
                    if(isDownloading){

                        randomAccessFile.write(buffer,0,length);
                        mCompleteSize+=length;
                        if(mCompleteSize<currentLength){
                            progress=(int)Float.parseFloat(getTowPointFloatStr(mCompleteSize*100/currentLength));
                            if(limit/30==0||progress<=100){
                                //限制notification的更新频率
                                sendProgressChangedMessage(progress);
                            }
                            limit++;
                        }
                    }
                }
                sendFinishMessage();
            }catch (IOException e){
                e.printStackTrace();
                sendFailureMessage(FailureCode.IO);
            }finally {
                try{
                    if(is!=null){
                        is.close();
                    }
                    if(randomAccessFile!=null){
                        randomAccessFile.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    sendFailureMessage(FailureCode.IO);
                }

            }
        }
    }
}
