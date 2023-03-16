package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClint {
    Socket socket = null;
    public TcpEchoClint(String severIp, int severPort) throws IOException {
        //Socket构造方法，可以识别点分十进制，不需要转换，比DatageamPacket方便
        //实例这个对象的同时，就会进行连接
        socket = new Socket(severIp, severPort);
    }
    public void start() {
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                //从控制台读取请求
                //空白字符结束，但不会读空白字符
                System.out.println("请输入请求：");
                String request = scanner.next();
                if(request.equals("exit")) {
                    System.out.println("bye bye");
                    break;
                }
                //发送请求
                PrintWriter printWriter = new PrintWriter(outputStream);
                //需要发送空白符，因为scanner需要空白符
                printWriter.println(request);
                printWriter.flush();
                //接收响应
                Scanner scanner1 = new Scanner(inputStream);
                String response = scanner1.next();
                System.out.println(response);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        TcpEchoClint tcpEchoClint = new TcpEchoClint("127.0.0.1", 8280);
        tcpEchoClint.start();
    }


}
