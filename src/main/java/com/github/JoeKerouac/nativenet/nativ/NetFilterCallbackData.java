package com.github.JoeKerouac.nativenet.nativ;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * netfilter回调数据
 * 
 * @author JoeKerouac
 * @version 2020年03月14日 18:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetFilterCallbackData {

    /**
     * ip报文数据
     */
    private byte[] data;

    /**
     * 数据长度，无符号short
     */
    private short dataLen;

    /**
     * netfilter队列号
     */
    private int queueNum;

    /**
     * 内核hook点
     * <li>0:NF_IP_PRE_ROUTING</li>
     * <li>1:NF_IP_LOCAL_IN</li>
     * <li>2:NF_IP_FORWARD</li>
     * <li>3:NF_IP_LOCAL_OUT</li>
     * <li>4:NF_IP_POST_ROUTING</li>
     */
    private int hookNum;

    /**
     * 数据包id，无符号int类型
     */
    private int id;
}
