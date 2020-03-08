package com.github.JoeKerouac.nativenet.nativ.impl;

import com.github.JoeKerouac.nativenet.nativ.ArpData;
import com.github.JoeKerouac.nativenet.nativ.NativeArpNetInterface;

/**
 * 本地实现
 *
 * @author JoeKerouac
 * @version 2020年03月08日 17:37
 */
public class NativeArpNetInterfaceImpl implements NativeArpNetInterface {

    static {
        System.loadLibrary("native/libcom_github_JoeKerouac_nativenet_nativ_impl_NativeArpNetInterfaceImpl.so");

    }

    public int createSock() {
        return _createSock();
    }

    public void close(int sock) {
        _close(sock);
    }

    public int sendArp(ArpData arpData, int sock) {
        return _sendArp(arpData, sock);
    }

    public ArpData receive_arp(int sock) {
        return _receive_arp(sock);
    }

    /**
     * 调用本地方法创建一个arp的socket
     * @return sock
     */
    native int _createSock();

    /**
     * 调用本地方法关闭一个arp的socket
     * @param sock sock
     */
    native void _close(int sock);

    /**
     * 调用本地方法发送arp请求
     * @param arpData 要发送的arp数据
     * @param sock 底层的sock
     * @return 返回0表示成功
     */
    native int _sendArp(ArpData arpData, int sock);

    /**
     * 调用本地方法接收一个ArpData
     *
     * @param sock 底层的sock
     * @return arp数据，返回null表示失败
     */
    native ArpData _receive_arp(int sock);
}
