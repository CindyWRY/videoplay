package com.test.videoplayserversdk.server.tcp.interf;


import com.test.videoplayserversdk.entity.Frame;

/**
 * @Desc    关于帧类型回调
 */

public interface OnAcceptBuffListener {
    void acceptBuff(Frame frame);
}
