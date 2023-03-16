package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpEchoSever {
    //Tcp协议服务器，使用ServerSocket类，来建立连接
    private ServerSocket serverSocket = null;
    public TcpEchoSever(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    public void start() throws IOException {
        System.out.println("启动服务器");
        //使用线程池，防止客户端数量过多，创建销毁大量线程开销太大
        //动态变化的线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        while (true) {
            //这里会阻塞，直到和客户端建立连接，返回Socket对象，来和客户端通信
            //客户端构造Socket对象时，会指定IP和端口，就会建立连接（客户端主动连接）
            Socket clintSocket = serverSocket.accept();
            threadPool.submit(() -> {
                try {
                    processConnection(clintSocket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            //要连接多个客户端，需要多线程去处理连接
            //这样才能让主线程继续执行到accept阻塞，然后和其他客户端建立连接（每个线程是独立的执行流，彼此之间是并发的关系）
            //如果客户端数量非常大，这里就会创建很多线程，数量过多对于系统来说也是很大的开销（使用线程池）
//            Thread t = new Thread(() -> {
//                try {
//                    processConnection(clintSocket);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            t.start();
        }
    }

    private void processConnection(Socket clintSocket) throws IOException {
        System.out.printf("【%s : %d】客户端上线\n", clintSocket.getInetAddress(), clintSocket.getPort());
        //读客户端请求
        //处理请求
        //将结果写回客户端（响应）
        try(InputStream inputStream = clintSocket.getInputStream();
            OutputStream outputStream = clintSocket.getOutputStream()) {

            //流式数据，循环读取
            while (true) {
                Scanner scanner = new Scanner(inputStream);
                //读取完毕，客户端下线
                if(!scanner.hasNext()) {
                    System.out.printf("【%s : %d】客户端下线\n", clintSocket.getInetAddress(), clintSocket.getPort());
                    break;
                }
                //读取请求
                // 注意!! 此处使用 next 是一直读取到换行符/空格/其他空白符结束, 但是最终返回结果里不包含上述 空白符 .
                String request = scanner.next();
                //处理请求
                String response = process(request);

                //写回客户端处理请求结果（响应）
                //为了直接写字符串，这里将字节流转换为字符流
                //也可以将字符串转为字节数组
                PrintWriter printWriter = new PrintWriter(outputStream);
                //写入且换行
                printWriter.println(response);
                //写入首先是写入了缓冲区，这里为了保险就刷新一下缓冲区
                printWriter.flush();
                System.out.printf("【%s : %d】请求：%s  响应：%s\n", clintSocket.getInetAddress(), clintSocket.getPort(),
                        request, response);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            //和一个客户端建立连接后，返回Socket对象（使用文件描述表），如果并发量大（会创建很多对象，文件描述符表就有可能满），就可能导致无法创建连接
            //因此需要保证资源得到释放，包裹在finally里
            clintSocket.close();
        }
    }
    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoSever tcpEchoSever = new TcpEchoSever(8280);
        tcpEchoSever.start();
    }
}
