package com.test.videoplayclientsdk.stream.sender.tcp.interf;



public interface OnTcpReadListener {

    void socketDisconnect();    //断开连接

    void connectSuccess();  //收到server消息,连接成功.
}
