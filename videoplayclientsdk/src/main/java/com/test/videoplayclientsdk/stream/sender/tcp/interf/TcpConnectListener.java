package com.test.videoplayclientsdk.stream.sender.tcp.interf;


public interface TcpConnectListener {
    void onSocketConnectSuccess();
    void onSocketConnectFail();
    void onTcpConnectSuccess();
    void onTcpConnectFail();
    void onPublishSuccess();
    void onPublishFail();
    void onSocketDisconnect();
}
