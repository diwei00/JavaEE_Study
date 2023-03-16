package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpChatClient {
    private Socket socket = null;
    private String severIp;
    private int severPort;
    public TcpChatClient(String severIp, int severPort) throws IOException {
        this.severIp = severIp;
        this.severPort = severPort;
        socket = new Socket(severIp, severPort);
    }
    public void start() throws IOException {
        //写外面。防止线程没有结束，就关闭了
        InputStream inputStream = socket.getInputStream();
        boolean flag = true;
        try(OutputStream outputStream = socket.getOutputStream()) {
            //线程始终检测有没有响应
            Thread t = new Thread(() -> {
                //接收响应
                while (true) {
                    Scanner scannerInput = new Scanner(inputStream);
                    //客户端下线
                    if(!scannerInput.hasNext()) {
                        System.out.println("客户端下线");
                        break;
                    }
                    String response = scannerInput.next();
                    System.out.println(response);
                }
                //只有客户端下线时,检测线程结束，才可以关闭
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            while (true) {
                //发送请求
                Scanner scanner = new Scanner(System.in);
                System.out.println("请输入要发送的消息：");
                String request = scanner.next();
                if(request.equals("拜拜")){
                    //断开与服务器连接
                    socket.close();
                    break;
                }
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println(request);
                printWriter.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入端口号：");
        int port = scanner.nextInt();
        TcpChatClient tcpChatClint = new TcpChatClient("101.42.36.220", port);
        tcpChatClint.start();
    }

}
