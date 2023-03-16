package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//回显服务器
//服务器工作流程：
//1）读取请求并解析
//2）根据请求计算响应
//3）构造响应并写回客户端
//注意：一个端口不能被多个进程使用（同一个主机），一个进程可以绑定多个端口（可以有多个Socket对象，Socket对象和端口是一一对应的）

public class UdpEchoSever {
    //Socket对象直接操作的是网卡，在操作系统中任务Socket对象是文件（一切皆文件）
    //通过Socket对象接收和发送数据
    private DatagramSocket socket = null;
    public UdpEchoSever(int port) throws SocketException {
        //服务器是被动的一方，客户端必须找到服务器的端口，才能找到指定程序，因此服务器必须指定端口号
        socket = new DatagramSocket(port);
    }
    public void start() throws IOException {
        System.out.println("启动服务器");
        while (true) {
            //构造空的Packet对象，传入缓冲数组
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096], 4096);
            //receive从网卡接收数据，解析后填充这个空对象（输出形参数）（可以认为写入了缓冲数组）
            //客户端如果没有发请求receive就会阻塞，直到客户端发送请求（保证这里不会一直循环）
            socket.receive(requestPacket);

            //根据接收的数据（由于接收的数据不方便处理），因此构造成字符串
            String request = new String(requestPacket.getData(), 0, requestPacket.getLength());
            //服务器响应处理
            String response = process(request);
            //构造发送的数据报，字节数组，字节数组长度，IP和端口（根据响应的字符串）
            //这个DatagramPacket只认字节数组，因此就需要获取字节数组的长度而不是字符的个数
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),response.getBytes().length,
                    requestPacket.getSocketAddress());
            //发送数据到Ip和端口指定的客户端程序
            socket.send(responsePacket);
            //打印下，请求响应的中间结果
            System.out.printf("源IP：%s 源端口：%d 请求数据：%s 响应数据：%s\n", requestPacket.getAddress().toString(),
                    requestPacket.getPort(), request, response);

        }
    }
    //回显服务器，处理直接返回数据（响应）
    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        //端口号的指定在 1024 -- 65535 里指定
        UdpEchoSever sever = new UdpEchoSever(8280);
        sever.start();
    }

}
