package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TcpChatSever {
    private ServerSocket serverSocket1 = null;
    private ServerSocket serverSocket2 = null;
    public TcpChatSever(int port) throws IOException {
        //建立两个端口，和两个客户端连接
        serverSocket1 = new ServerSocket(port);
        serverSocket2 = new ServerSocket(8281);

    }
    public void srart() throws IOException {
        System.out.println("服务器启动");
        ExecutorService threadPool = Executors.newCachedThreadPool();
        while (true) {
            Socket socket1 = serverSocket1.accept();
            Socket socket2 = serverSocket2.accept();
            //线程池处理
            threadPool.submit(() -> {
                try {
                    processConection(socket1, socket2);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void processConection(Socket socket1, Socket socket2) throws IOException, InterruptedException {
        System.out.printf("【IP：%s   port：%d】客户端1上线\n", socket1.getInetAddress().toString(), socket1.getPort());
        System.out.printf("【IP：%s   port：%d】客户端2上线\n", socket2.getInetAddress().toString(), socket2.getPort());
        InputStream inputStream1 = socket1.getInputStream();
        OutputStream outputStream1 = socket1.getOutputStream();
        InputStream inputStream2 = socket2.getInputStream();
        OutputStream outputStream2 = socket2.getOutputStream();

        //通过两个线程检测哪个客户端发起的请求
        //发送给对方客户端
        Thread t1 = new Thread(() -> {
            while (true) {
                Scanner scanner1 = new Scanner(inputStream1);
                if(!scanner1.hasNext()) {
                    System.out.printf("【%s : %d】客户端下线\n", socket1.getInetAddress(), socket1.getPort());
                    break;
                }
                //读取客户端请求
                String request1 = scanner1.next();

                //响应给对方方客户端
                process(request1, outputStream2);
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                Scanner scanner2 = new Scanner(inputStream2);
                if(!scanner2.hasNext()) {
                    System.out.printf("【%s : %d】客户端下线\n", socket2.getInetAddress(), socket2.getPort());
                    break;
                }
                String request2 = scanner2.next();
                process(request2, outputStream1);
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //防止还没有发送时就关闭了连接
        //不使用带有资源的try
        inputStream1.close();
        inputStream2.close();
        outputStream1.close();
        outputStream2.close();
        //服务端需要关闭，可能有大量用户
        socket1.close();
        socket2.close();

    }
    private void process(String request, OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println(request);
        printWriter.flush();
    }

    public static void main(String[] args) throws IOException {
        TcpChatSever tcpChatSever = new TcpChatSever(8280);
        tcpChatSever.srart();
    }
}
