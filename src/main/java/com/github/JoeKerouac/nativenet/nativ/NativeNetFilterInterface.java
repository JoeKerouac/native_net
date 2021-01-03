package com.github.JoeKerouac.nativenet.nativ;

import java.util.function.Consumer;

/**
 * 底层netfilter接口，没有任务逻辑，直接转发
 *
 * @author JoeKerouac
 * @version 2020年03月14日 18:15
 */
public interface NativeNetFilterInterface {

    /**
     * 启动队列，会一直阻塞直到退出
     * 
     * @param queueNum
     *            队列号
     */
    void run(int queueNum);

    /**
     * 注册回调函数
     * 
     * @param callback
     *            回调函数
     */
    void register(Consumer<NetFilterCallbackData> callback);

    /**
     * 发送数据决策
     * 
     * @param data
     *            要发送的数据
     * @param verdict
     *            决策，0-5
     *            <li>0:NF_DROP</li>
     *            <li>1:NF_ACCEPT</li>
     *            <li>2:NF_STOLEN</li>
     *            <li>3:NF_QUEUE</li>
     *            <li>4:NF_REPEAT</li>
     *            <li>5:NF_STOP</li>
     * @return 发送的数据长度，返回小于0表示发送失败
     */
    int sendVerdict(NetFilterCallbackData data, int verdict);
}
