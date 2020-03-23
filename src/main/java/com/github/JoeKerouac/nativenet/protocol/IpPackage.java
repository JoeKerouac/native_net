package com.github.JoeKerouac.nativenet.protocol;

import com.joe.utils.protocol.DatagramUtil;

/**
 * ip报文
 *
 * @author JoeKerouac
 * @version 2020年03月23日 21:24
 */
public class IpPackage {

    /**
     * 原始ip数据
     */
    private byte[] data;

    public IpPackage(byte[] data) {
        this.data = data;
    }

    /**
     * 获取版本号
     * @return 版本号，4位
     */
    public int getVersion() {
        return Byte.toUnsignedInt(data[0]) >> 4;
    }

    /**
     * 获取首部长度
     * @return 首部长度，4位
     */
    public int getHeaderLen() {
        return data[0] & 0x0f;
    }

    /**
     * 获取服务类型，目前一般不用
     * @return 服务类型，8位
     */
    public int getServiceType() {
        return data[1];
    }

    /**
     * 获取整个报文的长度
     * @return 报文长度，16位
     */
    public int getPackgeLen() {
        return Byte.toUnsignedInt(data[2]) << 8 | Byte.toUnsignedInt(data[3]);
    }

    /**
     * 标识字段，一般每发送一份报文值会加1
     * @return 标识字段，16位
     */
    public int getSeq() {
        return Byte.toUnsignedInt(data[4]) << 8 | Byte.toUnsignedInt(data[5]);
    }

    /**
     * ip报文类型
     * @return 总共3位，R、DF、MF三位。目前只有后两位有效
     * <li>DF位：为1表示不分片，为0表示分片</li>
     * <li>MF：为1表示“更多的片”，为0表示这是最后一片</li>
     */
    public int getFlag() {
        return Byte.toUnsignedInt(data[6]) >> 5;
    }

    /**
     * 获取片偏移，总共13位，如果数据分片表示分片在整个数据中的起始位置（第一片是0，每增加1表示多一个字节，即8bit）
     * @return 片偏移，总共13位
     */
    public int getOffset() {
        return (Byte.toUnsignedInt(data[6]) << 8 | Byte.toUnsignedInt(data[7])) & 0x1fff;
    }

    /**
     * 获取数据TTL
     * @return TTL，总共8位
     */
    public int getTtl() {
        return data[8];
    }

    /**
     * 获取子协议
     * @return 子协议，长度8位
     */
    public int getSubProtocol() {
        return data[9];
    }

    /**
     * 获取校验和
     * @return 校验和，总共16位
     */
    public int getCheckSum() {
        return Byte.toUnsignedInt(data[10]) << 8 | Byte.toUnsignedInt(data[11]);
    }

    /**
     * 获取源地址
     * @return 源地址
     */
    public int getSrcAdd() {
        return DatagramUtil.mergeToInt(data, 12);
    }

    /**
     * 获取目标地址
     * @return 目标地址
     */
    public int getDestAdd() {
        return DatagramUtil.mergeToInt(data, 16);
    }

}
