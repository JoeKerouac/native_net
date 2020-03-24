package com.github.JoeKerouac.nativenet.protocol;

import java.util.BitSet;

import com.joe.utils.common.Assert;

/**
 * @author JoeKerouac
 * @version 2020年03月24日 14:11
 */
public class AbstractPackage {

    /**
     * 原始网络数据
     */
    protected final byte[] data;

    /**
     * tcp数据的起始位置，单位byte
     */
    protected final int    offset;

    /**
     * 位数据
     */
    private final BitSet   bitSet;

    public AbstractPackage(byte[] data) {
        this(data, 0);
    }

    public AbstractPackage(byte[] data, int offset) {
        this.data = data;
        this.offset = offset;
        this.bitSet = BitSet.valueOf(data);
    }

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
        bitOffset += offset * 8;
        // 加上偏移后有可能溢出，这里强校验
        Assert.isTrue(bitOffset >= 0);
        // 一次只能读取32位
        Assert.isTrue(len <= 32 && len > 0);

        // 读取结束位
        int endBit = bitOffset + len;
        // 判断读取结束位不能溢出
        Assert.isTrue(endBit >= 0 && Math.ceil(endBit / 8.0) <= data.length);

        int result = 0;
        for (int i = bitOffset; i < endBit; i++) {
            result = result << 1 | (bitSet.get(i) ? 0 : 1);
        }
        return result;
    }
}
