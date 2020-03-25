package com.github.JoeKerouac.nativenet.protocol;

/**
 * 未知协议
 *
 * @author JoeKerouac
 * @version 2020年03月25日 13:39
 */
public class UnknownProtocol extends AbstractProtocol {

    public UnknownProtocol(byte[] data) {
        super(data);
    }

    public UnknownProtocol(byte[] data, int offset) {
        super(data, offset);
    }

    @Override
    public int getHeaderLen() {
        return 0;
    }
}
