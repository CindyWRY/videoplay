package com.pax.video.videoplay;


import android.app.Application;
import android.view.Surface;

import com.pax.commonlib.log.LogUtil;
import com.test.videoplayserversdk.ScreenRecordController;
import com.test.videoplayserversdk.control.VideoPlayController;
import com.test.videoplayserversdk.server.tcp.interf.OnServerStateChangeListener;

public class TcpVideoPlayService {
    private static final String TAG = "video";
    private Surface mSurface = null;
    private VideoPlayController mController;
    private Application mApplication;
 //   private boolean onConnected = false;

    private OnServerStateChangeListener mStateChangeListener = new OnServerStateChangeListener() {

        @Override
        public void acceptH264TcpConnect() {
            LogUtil.d(TAG, "accept a tcp connect...");
        }

        @Override
        public void acceptH264TcpDisConnect(Exception e) {
            LogUtil.d(TAG, "acceptTcpConnect exception = " + e.toString());
        }

        @Override
        public void exception() {

        }

        @Override
        public void acceptH264TcpNetSpeed(String netSpeed) {
            super.acceptH264TcpNetSpeed(netSpeed);
        }
    };

    public TcpVideoPlayService(Application application , Surface surface){
        mSurface = surface;
        mController = new VideoPlayController();
        mApplication = application;
        //LogUtil.d(TAG, "TcpVideoPlay Service start OK!");
        startTcpServer();
    }

    public void init(int width, int height){
        //mController.paramSet(width,height);
    }

    public void play(){
        mController.surfaceCreate(mSurface);
    }

    public void stop() {
        if (mController != null) mController.stop();
        ScreenRecordController.getInstance().stopServer();
    }

    private void startTcpServer() {
        // LogUtil.d(TAG, "startTcpServer Service start OK!");
        ScreenRecordController.getInstance()
                .init(mApplication)
                .setPort(11111)     //设置端口号
                .startServer()  //初始化,并开启server
                .setVideoPlayController(mController)    //设置VideoController
                .setOnAcceptTcpStateChangeListener(mStateChangeListener);   //设置回调
    }
}
