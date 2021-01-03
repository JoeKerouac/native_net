package com.github.JoeKerouac.nativenet.nativ;

/**
 * 本地arp接口
 *
 * @author JoeKerouac
 * @version 2020年03月08日 17:27
 */
public interface NativeArpNetInterface {

    /**
     * 创建一个arp的socket
     * 
     * @return sock
     */
    int createSock();

    /**
     * 关闭一个arp的socket
     * 
     * @param sock
     *            sock
     */
    void close(int sock);

    /**
     * 发送arp请求
     * 
     * @param arpData
     *            要发送的arp数据
     * @param sock
     *            底层的sock
     * @return 返回0表示成功
     */
    int sendArp(ArpData arpData, int sock);

    /**
     * 接收一个ArpData
     * 
     * @param sock
     *            底层的sock
     * @return arp数据，返回null表示失败
     */
    ArpData receive_arp(int sock);
}
