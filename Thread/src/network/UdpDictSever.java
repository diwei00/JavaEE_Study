package network;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

public class UdpDictSever extends UdpEchoSever{
    HashMap<String, String> hashMap = new HashMap<>();
    public UdpDictSever(int port) throws SocketException {
        super(port);

        hashMap.put("cat", "小猫");
        hashMap.put("dog", "小狗");
        hashMap.put("wuhao", "吴浩");
    }
    @Override
    public String process(String request) {

        return hashMap.getOrDefault(request, "没有查找到");
    }

    public static void main(String[] args) throws IOException {
        UdpDictSever udpDictSever = new UdpDictSever(8280);
        udpDictSever.start();

    }
}
