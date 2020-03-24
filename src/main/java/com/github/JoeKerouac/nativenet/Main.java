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
            try {
                IpPacket ipPackage = new IpPacket(data.getData());

                if (ipPackage.getSubPackage() != null) {
                    TcpSegment tcpPackage = ipPackage.getSubPackage();
                    // 只打印80端口的数据
                    if (tcpPackage.getDestPort() == 80) {
                        System.out
                            .println("源ip是：" + NetStringUtils.toIpString(ipPackage.getSrcAdd()));
                        System.out.println("\n");
                        System.out
                            .println("目标ip是：" + NetStringUtils.toIpString(ipPackage.getDestAdd()));
                        System.out.println("数据是：" + new String(tcpPackage.getPayload()));

                    }
                    System.out.println("\n\n\n\n\n");
                    System.out.println("决策成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            nativeNetFilterInterface.sendVerdict(data, 1);
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
