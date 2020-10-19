package com.test.videoplayserversdk.entity;

/**
 *
 * @Desc 返回一组解析后的数据
 */

public class ReceiveData {
    private ReceiveHeader header;
    private byte[] buff;

    public ReceiveHeader getHeader() {
        return header;
    }

    public void setHeader(ReceiveHeader header) {
        this.header = header;
    }

    public byte[] getBuff() {
        return buff;
    }

    public void setBuff(byte[] buff) {
        this.buff = buff;
    }
}
