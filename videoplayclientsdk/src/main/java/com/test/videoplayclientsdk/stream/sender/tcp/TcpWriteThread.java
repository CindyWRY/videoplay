package com.test.videoplayclientsdk.stream.sender.tcp;

import android.util.Log;

import com.test.videoplayclientsdk.stream.sender.rtmp.packets.Video;
import com.test.videoplayclientsdk.stream.sender.sendqueue.ISendQueue;
import com.test.videoplayclientsdk.stream.sender.tcp.interf.OnTcpWriteListener;
import com.test.videoplayclientsdk.entity.Frame;

import java.io.BufferedOutputStream;
import java.io.IOException;



public class TcpWriteThread extends Thread {
    private BufferedOutputStream bos;
    private ISendQueue iSendQueue;
    private volatile boolean startFlag;
    private OnTcpWriteListener mListener;
    private final String TAG = "TcpWriteThread";

    public TcpWriteThread(BufferedOutputStream bos, ISendQueue sendQueue, OnTcpWriteListener listener) {
        this.bos = bos;
        startFlag = true;
        this.iSendQueue = sendQueue;
        this.mListener = listener;
    }

    @Override
    public void run() {
        super.run();
        while (startFlag) {
            Frame frame = iSendQueue.takeFrame();
            if (frame == null) {
                continue;
            }
            if (frame.data instanceof Video) {
                sendData(((Video) frame.data).getData());
//                Log.e(TAG,"send a msg" );
            }
        }
    }


    public void shutDown() {
        startFlag = false;
        this.interrupt();
    }

    public void sendData(byte[] buff) {
        try {
            EncodeV1 encodeV1 = new EncodeV1(ScreenImageApi.encodeVersion1, ScreenImageApi.RECORD.MAIN_CMD,
                    ScreenImageApi.RECORD.SEND_BUFF, buff);
            bos.write(encodeV1.buildSendContent());
            bos.flush();
//            Log.e(TAG,"send data ");
        } catch (IOException e) {
            startFlag = false;
            Log.e("TcpWriteThread", "sendData Exception =" + e.toString());
            if (mListener != null) mListener.socketDisconnect();
        }
    }


}
