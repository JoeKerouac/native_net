package com.github.JoeKerouac.nativenet;

import java.util.List;

import com.github.JoeKerouac.nativenet.common.NetStringUtils;
import com.github.JoeKerouac.nativenet.nativ.ArpData;
import com.github.JoeKerouac.nativenet.nativ.ArpService;
import com.github.JoeKerouac.nativenet.nativ.NativeArpNetInterface;
import com.github.JoeKerouac.nativenet.nativ.impl.NativeArpNetInterfaceImpl;
import com.joe.utils.concurrent.ThreadUtil;

/**
 * @author JoeKerouac
 * @version 2020年03月08日 17:27
 */
public class Main {
    public static void main(String[] args) {
        NativeArpNetInterface nativeArpNetInterface = new NativeArpNetInterfaceImpl();
        int rcvSock = nativeArpNetInterface.createSock();

        // 开始扫描
        List<ArpData> allMacData = ArpService.getAllMac(NetStringUtils.toIpData("192.168.199.130"),
            NetStringUtils.toMacData("08:00:27:f6:f5:96"));

        allMacData.forEach(arpData -> {
            System.out.println("扫描到的源ip是：" + NetStringUtils.toIpString(arpData.getSrcIp()));
            System.out.println("扫描到的源mac是：" + NetStringUtils.toMacString(arpData.getSrcMac()));
            System.out.println("扫描到的目标ip是：" + NetStringUtils.toIpString(arpData.getDestIp()));
            System.out.println("扫描到的目标mac是：" + NetStringUtils.toMacString(arpData.getDestMac()));
            System.out.println("\n\n\n");
        });

        ThreadUtil.sleep(10);

        new Thread(() -> {
            while (true) {
                ArpData arpData = nativeArpNetInterface.receive_arp(rcvSock);
                System.out.println("接收到的源ip是：" + NetStringUtils.toIpString(arpData.getSrcIp()));
                System.out.println("接收到的源mac是：" + NetStringUtils.toMacString(arpData.getSrcMac()));
                System.out.println("接收到的目标ip是：" + NetStringUtils.toIpString(arpData.getDestIp()));
                System.out
                    .println("接收到的目标mac是：" + NetStringUtils.toMacString(arpData.getDestMac()));
                System.out.println("\n\n\n");
            }
        }).start();
    }
}
