package com.test.videoplayserversdk.control;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.test.videoplayserversdk.decode.DecodeThread;
import com.test.videoplayserversdk.entity.Frame;
import com.test.videoplayserversdk.mediacodec.VideoMediaCodec;
import com.test.videoplayserversdk.server.tcp.NormalPlayQueue;
import com.test.videoplayserversdk.server.tcp.interf.OnAcceptBuffListener;

/**
 * @Desc Surface绑定控制类
 */

public class VideoPlayController {
    private static final String TAG = "VideoPlayController";

    private VideoMediaCodec videoMediaCodec;
    private DecodeThread mDecodeThread;
    private NormalPlayQueue mPlayQueue;

    public VideoPlayController() {
        mPlayQueue = new NormalPlayQueue();

    }

    public void surfaceCreate(SurfaceHolder holder) {
        //初始化解码器
        Log.i(TAG, "create surface, and initial play queue");
        videoMediaCodec = new VideoMediaCodec(holder);
        //开启解码线程
        mDecodeThread = new DecodeThread(videoMediaCodec.getCodec(), mPlayQueue);
        videoMediaCodec.start();
        mDecodeThread.start();
    }

    public void surfaceCreate(Surface holder) {
        //初始化解码器
        Log.i(TAG, "create surface, and initial play queue");
        videoMediaCodec = new VideoMediaCodec(holder);
        //开启解码线程
        mDecodeThread = new DecodeThread(videoMediaCodec.getCodec(), mPlayQueue);
        videoMediaCodec.start();
        mDecodeThread.start();
    }

    public void surfaceDestrory() {
        mPlayQueue.stop();
        mDecodeThread.shutdown();
    }

    public void stop() {
        mPlayQueue.stop();
        mPlayQueue = null;
        mDecodeThread.shutdown();
    }

    public OnAcceptBuffListener getAcceptBuffListener() {
        return mAcceptBuffListener;
    }

    private OnAcceptBuffListener mAcceptBuffListener = new OnAcceptBuffListener() {
        @Override
        public void acceptBuff(Frame frame) {
            if (mPlayQueue != null) mPlayQueue.putByte(frame);
        }
    };

}
