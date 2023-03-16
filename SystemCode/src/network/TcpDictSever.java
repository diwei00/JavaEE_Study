package network;

import java.io.IOException;
import java.util.HashMap;

public class TcpDictSever extends TcpEchoSever{
    HashMap<String, String> hashMap = new HashMap<>();
    public TcpDictSever(int port) throws IOException {
        super(port);

        hashMap.put("cat", "小猫");
        hashMap.put("dog", "小狗");
        hashMap.put("pag", "小猪");
    }
    @Override
    public String process(String request) {
        String response = hashMap.getOrDefault(request, "没有找到该单词");

        return response;
    }

    public static void main(String[] args) throws IOException {
        TcpDictSever tcpDictSever = new TcpDictSever(8280);
        tcpDictSever.start();
    }
}
