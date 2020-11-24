package com.github.JoeKerouac.nativenet;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.JoeKerouac.nativenet.common.NetStringUtils;
import com.github.JoeKerouac.nativenet.nativ.ArpData;
import com.github.JoeKerouac.nativenet.nativ.NativeArpNetInterface;
import com.github.JoeKerouac.nativenet.nativ.NativeNetFilterInterface;
import com.github.JoeKerouac.nativenet.nativ.impl.NativeArpNetInterfaceImpl;
import com.github.JoeKerouac.nativenet.nativ.impl.NativeNetFilterInterfaceImpl;
import com.github.JoeKerouac.nativenet.protocol.IpPacket;
import com.github.JoeKerouac.nativenet.protocol.TcpSegment;
import com.joe.tls.InputRecordStream;
import com.joe.tls.OutputRecordStream;
import com.joe.utils.concurrent.ThreadUtil;
import org.apache.poi.hssf.record.RecordInputStream;

/**
 * @author JoeKerouac
 * @version 2020年03月08日 17:27
 */
public class Main {

    static {
        System.loadLibrary("arp_request_lib");
        System.loadLibrary("nf_userspace_queue");
    }

    private static Map<String, InputRecordStream> inputRecordStreamMap = new ConcurrentHashMap<>();
    private static Map<String, OutputRecordStream> outputRecordStreamMap = new ConcurrentHashMap<>();

    private static Map<String, InputStream> inputStreamMap = new ConcurrentHashMap<>();

    private static Map<String, OutputStream> outputStreamMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        netfilter();
    }

    static void netfilter() {
        NativeNetFilterInterface nativeNetFilterInterface = new NativeNetFilterInterfaceImpl();
        nativeNetFilterInterface.register(data -> {
            try {
                IpPacket ipPackage = new IpPacket(data.getData());

                if (ipPackage.getSubPackt() instanceof TcpSegment) {
                    TcpSegment tcpPackage = (TcpSegment) ipPackage.getSubPackt();
                    // 只打印80端口的数据
                    if (tcpPackage.getDestPort() == 80 || tcpPackage.getSrcPort() == 80) {
                        System.out
                            .println("源ip是：" + NetStringUtils.toIpString(ipPackage.getSrcAdd()));
                        System.out.println("\n");
                        System.out
                            .println("目标ip是：" + NetStringUtils.toIpString(ipPackage.getDestAdd()));
                        System.out.println("数据是：" + new String(tcpPackage.getPayload()));

                        System.out.println("\n\n\n\n\n");
                        System.out.println("决策成功");
                    } else if (tcpPackage.getDestPort() == 443 || tcpPackage.getSrcPort() == 443) {
                        int clientIp = tcpPackage.getSrcPort() == 443 ? ipPackage.getDestAdd() : ipPackage.getSrcAdd();
                        int clientPort = tcpPackage.getSrcPort() == 443 ? tcpPackage.getDestPort() : tcpPackage.getSrcPort();
                        int serverIp = tcpPackage.getSrcPort() == 443 ? ipPackage.getSrcAdd() : ipPackage.getDestAdd();
                        int serverPort = tcpPackage.getSrcPort() == 443 ? tcpPackage.getSrcPort() : tcpPackage.getDestPort();

                        String id = id(clientIp, clientPort, serverIp, serverPort);
                        InputStream inputStream = inputStreamMap.compute(id, (key, stream) -> {
                            if (stream == null) {
                                // TODO 这里要构造一个输入流
                                return null;
                            }else{
                                return stream;
                            }
                        });

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int result = nativeNetFilterInterface.sendVerdict(data, 1);
            if (result < 0) {
                System.out.println("发送失败了，结果：" + result);
                result = nativeNetFilterInterface.sendVerdict(data, 1);
                if (result < 0) {
                    System.out.println("发送重试仍然失败，结果：" + result);
                    System.out.println("要发送的数据是：" + data);
                    System.exit(result);
                }
            }
        });

        nativeNetFilterInterface.run(0);
    }

    static String id(int clientIp, int clientPort, int serverIp, int serverPort) {
        return String.format("%d:%d_%d:%d", clientIp, clientPort, serverIp, serverPort);
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
