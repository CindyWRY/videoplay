package com.pax.video.screencapture;

import android.media.projection.MediaProjection;

import com.test.videoplayclientsdk.configuration.AudioConfiguration;
import com.test.videoplayclientsdk.configuration.VideoConfiguration;
import com.test.videoplayclientsdk.controller.StreamController;
import com.test.videoplayclientsdk.controller.audio.NormalAudioController;
import com.test.videoplayclientsdk.controller.video.ScreenVideoController;
import com.test.videoplayclientsdk.stream.packer.Packer;
import com.test.videoplayclientsdk.stream.packer.tcp.TcpPacker;
import com.test.videoplayclientsdk.stream.sender.OnSenderListener;
import com.test.videoplayclientsdk.stream.sender.tcp.TcpSender;
import com.pax.commonlib.log.LogUtil;

import java.security.InvalidParameterException;


public class TcpScreenCaptureService  {
    private static final String TAG = "video";
    private StreamController mStreamController;
    private OnSenderListener mSenderListener;
    private VideoConfiguration mVideoConfiguration;
    private TcpSender mtcpSender;


    public TcpScreenCaptureService(MediaProjection mediaProjection){
        if(null == mediaProjection){
            throw new InvalidParameterException("MediaProjection is null");
        }

        mtcpSender = new TcpSender();
        mSenderListener = new OnSenderListener(){

            @Override
            public void onConnecting() {
                LogUtil.i(TAG, "onConnecting ...");
            }

            @Override
            public void onConnected() {
                LogUtil.i(TAG, "onConnected");
            }

            @Override
            public void onDisConnected() {
                LogUtil.i(TAG, "onDisConnected");
            }

            @Override
            public void onPublishFail() {
                LogUtil.e(TAG, "onPublishFail");
            }

            @Override
            public void onNetGood() {

            }

            @Override
            public void onNetBad() {

            }
        };
        NormalAudioController audioController = new NormalAudioController();
        ScreenVideoController videoController = new ScreenVideoController(mediaProjection);
        mStreamController = new StreamController(videoController, audioController);
        mVideoConfiguration = new VideoConfiguration.Builder().build();
    }

    public void setVideoConfiguration(VideoConfiguration videoConfiguration) {
        if(mStreamController != null) {
            mStreamController.setVideoConfiguration(videoConfiguration);
        }
    }

    public void setTcpIp(String ip){
         mtcpSender.setTcpIp(ip);
    }


    public void startRecord() {
        TcpPacker packer = new TcpPacker();
        packer.setSendAudio(true);
        packer.initAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        setRecordPacker(packer);

        setVideoConfiguration(mVideoConfiguration);
        mtcpSender.setSenderListener(mSenderListener);
        mtcpSender.connect();

        if(mStreamController != null) {
            mStreamController.setSender(mtcpSender);
            mStreamController.start();
        }
    }

    public void stopRecord() {
        if(mStreamController != null) {
            mStreamController.stop();
        }
    }

    private void setRecordPacker(Packer packer) {
        if(mStreamController != null) {
            mStreamController.setPacker(packer);
        }
    }
}
