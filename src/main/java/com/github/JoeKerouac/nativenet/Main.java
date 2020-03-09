package com.github.JoeKerouac.nativenet;

import com.github.JoeKerouac.nativenet.common.NetStringUtils;
import com.github.JoeKerouac.nativenet.nativ.ArpData;
import com.github.JoeKerouac.nativenet.nativ.NativeArpNetInterface;
import com.github.JoeKerouac.nativenet.nativ.impl.NativeArpNetInterfaceImpl;

/**
 * @author JoeKerouac
 * @version 2020年03月08日 17:27
 */
public class Main {
    public static void main(String[] args) {
        NativeArpNetInterface nativeArpNetInterface = new NativeArpNetInterfaceImpl();
        int sock = nativeArpNetInterface.createSock();
        System.out.println("sock 是 " + sock);

        new Thread(() -> {
            while (true) {
                ArpData arpData = nativeArpNetInterface.receive_arp(sock);
                System.out.println("接收到的源ip是：" + NetStringUtils.toIpString(arpData.getSrcIp()));
                System.out.println("接收到的目标ip是：" + NetStringUtils.toIpString(arpData.getDestIp()));
            }
        }).start();
    }
}
