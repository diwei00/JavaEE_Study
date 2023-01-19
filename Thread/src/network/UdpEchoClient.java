package network;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

//回显客户端
//客户端和服务器在同一个主机上，因此IP都是127.0.0.1（换汇IP）
//任何一次通信源端口，IP都是两个（源端口，目的端口，源IP，，目的IP）
public class UdpEchoClient {
    private DatagramSocket socket = null;
    //客户端需要知道服务器的IP，和端口，这里先存一下
    private String severIp = null;
    private int severPort = 0;
    public UdpEchoClient(String severIp, int severPort) throws SocketException {
        //客户端不需要指定端口号，客户端程序在用户手里，指定端口号就可能和其他进程重复。因此让操作系统分配一个空闲的端口
        //服务器为什么指定端口不怕重复呢？因为服务器在程序员手里我们清楚端口号的使用（可控的），而客户端是（不可控的）
        socket = new DatagramSocket();
        this.severIp = severIp;
        this.severPort = severPort;
    }
    //客户端启动
    public void start() throws IOException {
        //用户输入数据
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("输入你要发送的数据：");
            String request = scanner.next();
            if (request.equals("exit")) {
                System.out.println("bye bye");
                break;
            }
            //发送数据报（构造DatagramPacket对象）
            //此处的IP需要一个32位的整数，而上面的是字符串，需要转换
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(), request.getBytes().length,
                    InetAddress.getByName(severIp), severPort);
            socket.send(requestPacket);

            //接收数据报（填充这个空对象）（阻塞到服务器发送过来数据）
            //receive的阻塞操作系统实现的，JAVA只是封装了一下
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096], 4096);
            socket.receive(responsePacket);
            //显示数据报（将数据报转换为字符串）
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient udpEchoClient = new UdpEchoClient("127.0.0.1", 8280);
        udpEchoClient.start();
    }
}
