package com.github.JoeKerouac.nativenet;

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
        nativeArpNetInterface.close(sock);
        System.out.println("成功了");
    }
}
