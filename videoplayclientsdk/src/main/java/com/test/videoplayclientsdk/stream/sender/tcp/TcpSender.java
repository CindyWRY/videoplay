package com.test.videoplayclientsdk.stream.sender.tcp;


import com.test.videoplayclientsdk.configuration.VideoConfiguration;
import com.test.videoplayclientsdk.stream.sender.rtmp.packets.Chunk;
import com.test.videoplayclientsdk.stream.sender.rtmp.packets.Video;
import com.test.videoplayclientsdk.stream.sender.sendqueue.ISendQueue;
import com.test.videoplayclientsdk.stream.sender.sendqueue.NormalSendQueue;
import com.test.videoplayclientsdk.stream.sender.sendqueue.SendQueueListener;
import com.test.videoplayclientsdk.stream.sender.tcp.interf.TcpConnectListener;
import com.test.videoplayclientsdk.entity.Frame;
import com.test.videoplayclientsdk.stream.packer.tcp.TcpPacker;
import com.test.videoplayclientsdk.stream.sender.OnSenderListener;
import com.test.videoplayclientsdk.stream.sender.Sender;
import com.test.videoplayclientsdk.utils.WeakHandler;


public class TcpSender implements Sender, SendQueueListener {
    private ISendQueue mSendQueue = new NormalSendQueue();
    private static final String TAG = "TcpSender";
    private OnSenderListener sendListener;
    private TcpConnection mTcpConnection;
    private WeakHandler weakHandler = new WeakHandler();
    private String ip;
    private int port = 11111;;


    public TcpSender() {
        mTcpConnection = new TcpConnection();
        //this.ip = ip;
        //this.port = port;
    }

    @Override
    public void start() {
        mSendQueue.setSendQueueListener(this);
        mSendQueue.start();
    }

    public void setVideoParams(VideoConfiguration videoConfiguration) {
        mTcpConnection.setVideoParams(videoConfiguration);
    }
    public void setTcpIp(String ip){
        this.ip = ip;
    }
    @Override
    public void onData(byte[] data, int type) {
        Frame<Chunk> frame = null;
        Video video = new Video();
        video.setData(data);
        if (type == TcpPacker.FIRST_VIDEO) {
            frame = new Frame(video, type, Frame.FRAME_TYPE_CONFIGURATION);
        } else if (type == TcpPacker.KEY_FRAME) {
            frame = new Frame(video, type, Frame.FRAME_TYPE_KEY_FRAME);
        } else if (type == TcpPacker.INTER_FRAME) {
            frame = new Frame(video, type, Frame.FRAME_TYPE_INTER_FRAME);
        } else if (type == TcpPacker.AUDIO) {
            frame = new Frame(video, type, Frame.FRAME_TYPE_AUDIO);
        }
        if (frame == null) return;
        mSendQueue.putFrame(frame);
    }

    @Override
    public void stop() {
        mTcpConnection.stop();
        mSendQueue.stop();
    }

    public void connect() {
        mTcpConnection.setSendQueue(mSendQueue);
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectNotInUi();
            }
        }).start();

    }

    private synchronized void connectNotInUi() {
        mTcpConnection.setConnectListener(mTcpListener);
        mTcpConnection.connect(ip, port);
    }

    @Override
    public void good() {
        weakHandler.post(new Runnable() {
            @Override
            public void run() {
                sendListener.onNetGood();
            }
        });
    }

    @Override
    public void bad() {
        weakHandler.post(new Runnable() {
            @Override
            public void run() {
                sendListener.onNetBad();
            }
        });
    }

    private TcpConnectListener mTcpListener = new TcpConnectListener() {
        @Override
        public void onSocketConnectSuccess() {
//            Log.e(TAG, "onSocketConnectSuccess");
        }

        @Override
        public void onSocketConnectFail() {
//            Log.e(TAG, "onSocketConnectFail");
            disConnected();
        }

        @Override
        public void onTcpConnectSuccess() {
//            Log.e(TAG, "onTcpConnectSuccess");
        }

        @Override
        public void onTcpConnectFail() {
//            Log.e(TAG, "onTcpConnectFail");
            disConnected();
        }

        @Override
        public void onPublishSuccess() {
//            Log.e(TAG, "onPublishSuccess");
            weakHandler.post(new Runnable() {
                @Override
                public void run() {
                    sendListener.onConnected();
                }
            });
        }

        @Override
        public void onPublishFail() {
//            Log.e(TAG, "onPublishFail");
            weakHandler.post(new Runnable() {
                @Override
                public void run() {
                    sendListener.onPublishFail();
                }
            });
        }

        @Override
        public void onSocketDisconnect() {
//            Log.e(TAG, "onSocketDisconnect");
            disConnected();
        }

    };

    private void disConnected() {
        weakHandler.post(new Runnable() {
            @Override
            public void run() {
                sendListener.onDisConnected();
            }
        });
    }


    public void setSenderListener(OnSenderListener listener) {
        this.sendListener = listener;
    }

    /**
     * add by xu.wang 为解决首次黑屏而加
     */
    public void setSpsPps(byte[] spsPps) {
        if (mTcpConnection != null) mTcpConnection.setSpsPps(spsPps);
    }

}
