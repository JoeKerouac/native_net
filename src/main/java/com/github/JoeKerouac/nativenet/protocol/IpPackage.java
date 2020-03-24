package com.github.JoeKerouac.nativenet.protocol;

/**
 * ip报文
 *
 * @author JoeKerouac
 * @version 2020年03月23日 21:24
 */
public class IpPackage extends AbstractPackage {

    public IpPackage(byte[] data) {
        super(data);
    }

    public IpPackage(byte[] data, int offset) {
        super(data, offset);
    }

    /**
     * 获取版本号
     * @return 版本号，4位
     */
    public int getVersion() {
        return readBit(0, 4);
    }

    /**
     * 获取首部长度
     * @return 首部长度，4位
     */
    public int getHeaderLen() {
        return readBit(4, 4);
    }

    /**
     * 获取服务类型，目前一般不用
     * @return 服务类型，8位
     */
    public int getServiceType() {
        return readBit(8, 8);
    }

    /**
     * 获取整个报文的长度
     * @return 报文长度，16位
     */
    public int getPackgeLen() {
        return readBit(16, 16);
    }

    /**
     * 标识字段，一般每发送一份报文值会加1
     * @return 标识字段，16位
     */
    public int getSeq() {
        return readBit(32, 16);
    }

    /**
     * ip报文类型
     * @return 总共3位，R、DF、MF三位。目前只有后两位有效
     * <li>DF位：为1表示不分片，为0表示分片</li>
     * <li>MF：为1表示“更多的片”，为0表示这是最后一片</li>
     */
    public int getFlag() {
        return readBit(48, 3);
    }

    /**
     * 获取片偏移，总共13位，如果数据分片表示分片在整个数据中的起始位置（第一片是0，每增加1表示多一个字节，即8bit）
     * @return 片偏移，总共13位
     */
    public int getOffset() {
        return readBit(51, 13);
    }

    /**
     * 获取数据TTL
     * @return TTL，总共8位
     */
    public int getTtl() {
        return readBit(64, 8);
    }

    /**
     * 获取子协议
     * @return 子协议，长度8位
     * <li>1:ICMP协议</li>
     * <li>2:IGMP协议</li>
     * <li>6:TCP协议</li>
     * <li>17:UDP协议</li>
     */
    public int getSubProtocol() {
        return readBit(72, 8);
    }

    /**
     * 获取校验和
     * @return 校验和，总共16位
     */
    public int getCheckSum() {
        return readBit(80, 16);
    }

    /**
     * 获取源地址
     * @return 源地址，32位，无符号
     */
    public int getSrcAdd() {
        return readBit(96, 32);
    }

    /**
     * 获取目标地址
     * @return 目标地址，32位，无符号
     */
    public int getDestAdd() {
        return readBit(128, 32);
    }

    /**
     * 获取子协议数据
     * @return 子协议数据，子协议不支持的时候将会返回null
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractPackage> T getSubPackage() {
        if (getSubProtocol() == 6) {
            return (T) new TcpPackage(data, getHeaderLen());
        }
        return null;
    }

}
