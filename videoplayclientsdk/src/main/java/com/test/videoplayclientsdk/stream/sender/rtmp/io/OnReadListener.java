package com.test.videoplayclientsdk.stream.sender.rtmp.io;

import com.test.videoplayclientsdk.stream.sender.rtmp.packets.Chunk;


public interface OnReadListener {
    void onChunkRead(Chunk chunk);
    void onDisconnect();
    void onStreamEnd();
}
