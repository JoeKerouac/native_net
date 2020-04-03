package com.github.JoeKerouac.nativenet.protocol;

import java.util.Arrays;

import com.joe.utils.common.Assert;

/**
 * 抽象协议栈，一般第二层数据链路层叫Frame，第三层网络层叫Packet，第四层传输层叫Segment
 *
 * @author JoeKerouac
 * @version 2020年03月24日 14:11
 */
public abstract class AbstractProtocol {

    /**
     * 对应二进制的 1000 0000，定义常量方便后边使用
     */
    private static final int FLAG = 1 << 7;

    /**
     * 原始网络数据
     */
    protected final byte[]   data;

    /**
     * tcp数据的起始位置，单位byte
     */
    protected final int      offset;

    public AbstractProtocol(byte[] data) {
        this(data, 0);
    }

    public AbstractProtocol(byte[] data, int offset) {
        this.data = data;
        this.offset = offset;
    }

    /**
     * 获取子协议对象
     * @return 子协议对象，无法解析出子协议时将会返回null
     */
    public AbstractProtocol getSubPackt() {
        // 默认无法解析子协议，如果可以解析子协议那么请实现
        return null;
    }

    /**
     * 获取负载数据，不包含协议头
     * @return 负载数据，返回的是copy数据，不应该直接透出
     */
    public byte[] getPayload() {
        return Arrays.copyOfRange(data, offset + getHeaderLen(), data.length);
    }

    /**
     * 获取协议头长度
     * @return 协议头长度，单位byte
     */
    public abstract int getHeaderLen();

    /**
     * 从指定bitOffset开始读取，读取长度len的数据
     * @param bitOffset 起始bit位
     * @param len 读取长度，单位bit
     * @return 读取到的数据
     */
    protected int readBit(int bitOffset, int len) {
        // 边界校验
        Assert.isTrue(bitOffset >= 0);
        // 加上偏移
        bitOffset += offset << 3;
        // 加上偏移后有可能溢出，这里强校验
        Assert.isTrue(bitOffset >= 0);
        // 一次最多只能读取32位
        Assert.isTrue(len <= 32 && len > 0);

        // 读取结束位
        int endBit = bitOffset + len;
        // 判断读取结束位不能溢出
        Assert.isTrue(endBit >= 0 && ((endBit + 7) >> 3) <= data.length);

        int result = 0;
        for (int i = bitOffset; i < endBit; i++) {
            // 注意，bitSet是小端排序
            result = result << 1 | (readBit(i) ? 1 : 0);
        }
        return result;
    }

    /**
     * 从当前数据中读取指定下标的bit数据
     * @param bitIndex bitIndex
     * @return 如果该位是1则返回true，否则返回false
     */
    private boolean readBit(int bitIndex) {
        int wordIndex = bitIndex >> 3;

        int index = bitIndex % 8;

        return (Byte.toUnsignedInt(data[wordIndex]) & FLAG >>> index) > 0;
    }
}
