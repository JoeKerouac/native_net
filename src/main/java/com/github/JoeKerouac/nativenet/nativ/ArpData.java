package com.github.JoeKerouac.nativenet.nativ;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author JoeKerouac
 * @version 2020年03月08日 17:32
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ArpData {

    /**
     * 请求方ip
     */
    private byte[] srcIp;

    /**
     * 请求方mac
     */
    private byte[] srcMac;

    /**
     * 接收方ip
     */
    private byte[] destIp;

    /**
     * 接受方mac
     */
    private byte[] destMac;
}
