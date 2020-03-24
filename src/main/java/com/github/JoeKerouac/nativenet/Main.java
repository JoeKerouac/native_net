package com.github.JoeKerouac.nativenet;

import java.util.List;

import com.github.JoeKerouac.nativenet.common.NetStringUtils;
import com.github.JoeKerouac.nativenet.nativ.ArpData;
import com.github.JoeKerouac.nativenet.nativ.NativeArpNetInterface;
import com.github.JoeKerouac.nativenet.nativ.NativeNetFilterInterface;
import com.github.JoeKerouac.nativenet.nativ.impl.NativeArpNetInterfaceImpl;
import com.github.JoeKerouac.nativenet.nativ.impl.NativeNetFilterInterfaceImpl;
import com.github.JoeKerouac.nativenet.protocol.IpPacket;
import com.github.JoeKerouac.nativenet.protocol.TcpSegment;
import com.joe.utils.concurrent.ThreadUtil;

/**
 * @author JoeKerouac
 * @version 2020年03月08日 17:27
 */
public class Main {
    public static void main(String[] args) {
        netfilter();
    }

    static void netfilter() {
        NativeNetFilterInterface nativeNetFilterInterface = new NativeNetFilterInterfaceImpl();
        nativeNetFilterInterface.register(data -> {
            System.out.println("收到数据是：\n\n" + data);
            IpPacket ipPackage = new IpPacket(data.getData());
            System.out.println("源ip是：" + NetStringUtils.toIpString(ipPackage.getSrcAdd()));
            System.out.println("目标ip是：" + NetStringUtils.toIpString(ipPackage.getDestAdd()));
            System.out.println("子协议：" + ipPackage.getSubProtocol());
            if (ipPackage.getSubPackage() != null) {
                TcpSegment tcpPackage = ipPackage.getSubPackage();
                System.out.println("目标端口是：" + tcpPackage.getDestPort());
                System.out.println("源端口是：" + tcpPackage.getSrcPort());
                System.out.println("flag是：" + tcpPackage.getFlag());
            }
            System.out.println("\n\n\n\n\n");
            nativeNetFilterInterface.sendVerdict(data, 1);
            System.out.println("发送决策成功");
        });

        nativeNetFilterInterface.run(0);
    }

    static void scan() {
        NativeArpNetInterface nativeArpNetInterface = new NativeArpNetInterfaceImpl();
        int rcvSock = nativeArpNetInterface.createSock();

        // 开始扫描
        List<ArpData> allMacData = ArpService.getAllMac("192.168.199.130",
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
