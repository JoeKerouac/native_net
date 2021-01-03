package com.github.JoeKerouac.nativenet.protocol;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author JoeKerouac
 * @version 2020年03月25日 13:36
 */
public class IpPacketTest {

    /**
     * 一份ip报文（随机抓取的），对应的子协议是TCP协议
     */
    private static final byte[] IP_PACKT = new byte[] {69, 0, 1, 69, 0, 0, 64, 0, 63, 6, 61, 45, -64, -88, -57, -67,
        -53, 119, -87, -88, -6, 109, 0, 80, -111, 79, -91, -39, -105, 51, -126, 105, 80, 24, 16, 0, -51, -94, 0, 0, 80,
        79, 83, 84, 32, 47, 97, 32, 72, 84, 84, 80, 47, 49, 46, 49, 13, 10, 72, 111, 115, 116, 58, 32, 111, 99, 115, 45,
        111, 110, 101, 97, 103, 101, 110, 116, 45, 115, 101, 114, 118, 101, 114, 46, 97, 108, 105, 98, 97, 98, 97, 46,
        99, 111, 109, 13, 10, 67, 111, 110, 116, 101, 110, 116, 45, 84, 121, 112, 101, 58, 32, 97, 112, 112, 108, 105,
        99, 97, 116, 105, 111, 110, 47, 106, 115, 111, 110, 13, 10, 67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 58,
        32, 107, 101, 101, 112, 45, 97, 108, 105, 118, 101, 13, 10, 65, 99, 99, 101, 112, 116, 58, 32, 42, 47, 42, 13,
        10, 85, 115, 101, 114, 45, 65, 103, 101, 110, 116, 58, 32, 40, 110, 117, 108, 108, 41, 47, 40, 110, 117, 108,
        108, 41, 32, 40, 77, 97, 99, 32, 79, 83, 32, 88, 32, 86, 101, 114, 115, 105, 111, 110, 32, 49, 48, 46, 49, 52,
        46, 54, 32, 40, 66, 117, 105, 108, 100, 32, 49, 56, 71, 49, 48, 51, 41, 41, 13, 10, 65, 99, 99, 101, 112, 116,
        45, 76, 97, 110, 103, 117, 97, 103, 101, 58, 32, 122, 104, 45, 72, 97, 110, 115, 45, 67, 78, 59, 113, 61, 49,
        13, 10, 65, 99, 99, 101, 112, 116, 45, 69, 110, 99, 111, 100, 105, 110, 103, 58, 32, 103, 122, 105, 112, 44, 32,
        100, 101, 102, 108, 97, 116, 101, 13, 10, 67, 111, 110, 116, 101, 110, 116, 45, 76, 101, 110, 103, 116, 104, 58,
        32, 49, 53, 54, 51, 50, 13, 10, 13, 10};

    @Test
    public void test() {
        IpPacket packet = new IpPacket(IP_PACKT);

        Assert.assertEquals(packet.getVersion(), 4);
        Assert.assertEquals(packet.getHeaderLen(), 20);
        Assert.assertEquals(packet.getServiceType(), 0);
        Assert.assertEquals(packet.getPackgeLen(), 325);
        Assert.assertEquals(packet.getSeq(), 0);
        Assert.assertEquals(packet.getFlag(), 2);
        Assert.assertEquals(packet.getOffset(), 0);
        Assert.assertEquals(packet.getTtl(), 63);
        Assert.assertEquals(packet.getSubProtocol(), 6);
        Assert.assertEquals(packet.getCheckSum(), 15661);
        Assert.assertEquals(packet.getSrcAdd(), -1062680643);
        Assert.assertEquals(packet.getDestAdd(), -881350232);
        Assert.assertTrue(packet.getSubPackt() instanceof TcpSegment);

        TcpSegment segment = (TcpSegment)packet.getSubPackt();

        Assert.assertEquals(segment.getSrcPort(), 64109);
        Assert.assertEquals(segment.getDestPort(), 80);
        Assert.assertEquals(segment.getReqSeq(), -1857051175);
        Assert.assertEquals(segment.getAckSeq(), -1758231959);
        Assert.assertEquals(segment.getHeaderLen(), 20);
        Assert.assertEquals(segment.getReserved(), 0);
        Assert.assertEquals(segment.getFlag(), 24);
        Assert.assertEquals(segment.getWindowSize(), 4096);
        Assert.assertEquals(segment.getCheckSum(), 52642);
        Assert.assertEquals(segment.getUrgentPoint(), 0);
    }
}
