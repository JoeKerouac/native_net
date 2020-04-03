package com.github.JoeKerouac.nativenet.protocol;

/**
 * TCP报文
 *
 * @author JoeKerouac
 * @version 2020年03月24日 09:28
 */
public class TcpSegment extends AbstractProtocol {

    public TcpSegment(byte[] data) {
        super(data);
    }

    public TcpSegment(byte[] data, int offset) {
        super(data, offset);
    }

    /**
     * 获取源端口号
     * @return 源端口号，16位
     */
    public int getSrcPort() {
        return readBit(0, 16);
    }

    /**
     * 获取目标端口号
     * @return 目标端口号，16位
     */
    public int getDestPort() {
        return readBit(16, 16);
    }

    /**
     * 获取请求序列号
     * <p></p>
     * 在TCP传送的流中，每一个字节一个序号。e.g.一个报文段的序号为300，此报文段数据部分共有100字节，则下一个报文段的序号为400
     * @return 请求序列号，32位无符号
     */
    public int getReqSeq() {
        return readBit(32, 32);
    }

    /**
     * 获取响应序列号
     * @return 响应序列号，32位无符号
     */
    public int getAckSeq() {
        return readBit(64, 32);
    }

    /**
     * 获取首部长度
     * @return 首部长度，4位
     */
    @Override
    public int getHeaderLen() {
        // 注意，这里在tcp协议中单位是4byte，而不是1byte，所以要乘以4
        return readBit(96, 4) * 4;
    }

    /**
     * 获取保留位
     * @return 保留位，6位
     */
    public int getReserved() {
        return readBit(100, 6);
    }

    /**
     * 获取标志位
     * @return 标志位，6位，标志位对应信息如下(可能同时出现两个标志位，比如24，表示ACK|PSH)：
     * <li>1:FIN</li>
     * <li>2:SYN</li>
     * <li>4:RST</li>
     * <li>8:PSH</li>
     * <li>16:ACK</li>
     * <li>32:URG</li>
     */
    public int getFlag() {
        return readBit(106, 6);
    }

    /**
     * 获取窗口大小
     * @return 窗口大小，16位
     */
    public int getWindowSize() {
        return readBit(112, 16);
    }

    /**
     * 获取校验和
     * @return 校验和，16位
     */
    public int getCheckSum() {
        return readBit(128, 16);
    }

    /**
     * 获取紧急指针
     * @return 紧急指针，16位
     */
    public int getUrgentPoint() {
        return readBit(144, 16);
    }

}
