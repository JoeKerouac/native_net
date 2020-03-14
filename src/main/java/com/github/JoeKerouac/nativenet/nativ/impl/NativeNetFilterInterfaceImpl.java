package com.github.JoeKerouac.nativenet.nativ.impl;

import java.util.function.Consumer;

import com.github.JoeKerouac.nativenet.nativ.NativeNetFilterInterface;
import com.github.JoeKerouac.nativenet.nativ.NetFilterCallbackData;

/**
 * 底层netfilter接口，没有任务逻辑，直接转发
 * 
 * @author JoeKerouac
 * @version 2020年03月14日 18:51
 */
public class NativeNetFilterInterfaceImpl implements NativeNetFilterInterface {

    @Override
    public void run(int queueNum) {
        _run(queueNum);
    }

    @Override
    public void register(Consumer<NetFilterCallbackData> callback) {
        _register(callback);
    }

    @Override
    public void sendVerdict(NetFilterCallbackData data, int verdict) {
        _sendVerdict(data, verdict);
    }

    private native void _run(int queueNum);

    native void _register(Consumer<NetFilterCallbackData> callback);

    native void _sendVerdict(NetFilterCallbackData data, int verdict);
}
