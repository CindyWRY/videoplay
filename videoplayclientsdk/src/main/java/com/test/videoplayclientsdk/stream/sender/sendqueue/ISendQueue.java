package com.test.videoplayclientsdk.stream.sender.sendqueue;

import com.test.videoplayclientsdk.entity.Frame;


public interface ISendQueue {
    void start();
    void stop();
    void setBufferSize(int size);
    void putFrame(Frame frame);
    Frame takeFrame();
    void setSendQueueListener(SendQueueListener listener);
}
