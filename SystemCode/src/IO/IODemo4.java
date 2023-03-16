package IO;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

//频繁的操作IO是需要时间的，我们需要尽量减少IO次数
public class IODemo4 {
    public static void main(String[] args) throws IOException {
        //Scanner的参数也是流对象，System.in是标准输入流（键盘），我们也可以指定流对象
        File file = new File("E:/tmp/test.txt");
        try(Reader reader = new FileReader(file)) {
            //指定reader就存reader流对象里读
            Scanner scanner = new Scanner(reader);
            String str = scanner.next();
            System.out.println(str);
        }

    }
    public static void main6(String[] args) {
        File file = new File("E:/tmp/test.txt");
        //字符流读
        try(Reader reader = new FileReader(file)) {
            while (true) {
                char[] buffer = new char[1024];
                int ret = reader.read(buffer);
                if (ret == -1) {
                    break;
                }
                System.out.println(ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main5(String[] args) throws IOException {
        File file = new File("E:/tmp/test.txt");
        //字符写流写
        try(Writer writer = new FileWriter(file)) {
            String ret = "吴浩";
            writer.write(ret);
        }

    }
    public static void main4(String[] args) throws IOException {
        //为了保证close被执行使用带有资源的try：try() {}
        //只有实现了Closeable类的方法，才可以这样写
        //这样会自动执行close()方法
        File file = new File("E:/tmp/test.txt");
        try(OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(97);
            //手动刷新缓冲区
            outputStream.flush();
        }
        //写操作，实际是将内容先写入缓冲区，等待缓冲区到达了一定程度，就会刷新到硬盘中
        //flush()方法手动刷新缓冲区
    }
    public static void main3(String[] args) throws IOException {
        File file = new File("E:/tmp/test.txt");
        //OutputStream指向的文件如果没有，就会自动创建
        OutputStream outputStream = new FileOutputStream(file);
        //在写的时候，会清空原来的文件内容
        outputStream.write(97);
        outputStream.write(98);
        outputStream.write(99);
        outputStream.write(100);
        //将整个数组写入文件中
        byte[] arr = {'a', 'b'};
        outputStream.write(arr);

        //这里关闭的其实是一个进程pcb中的“文件描述符表”，这其实是一个有边界的数组（大小可以改变），会释放数组中表示打开文件的对象
        //当打开一个文件，就会向这里的文件描述符表申请一块资源
        //一个进程中所有线程共用一个文件描述符表，如果表满了，则打开文件就会失败
        //为了保证close被执行使用带有资源的try：try() {}

        outputStream.close();

    }
    public static void main2(String[] args) throws IOException {
        File file = new File("E:/tmp/dog.png");
        InputStream inputStream = new FileInputStream(file);
        while (true) {
            //缓冲区数组。可以减少IO次数，相比于一次读一个字节
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            System.out.println(len);
            if(len == -1) {
                break;
            }
        }
        inputStream.close();
    }
    public static void main1(String[] args) throws IOException {
        File file = new File("E:/tmp/test.txt");
        InputStream inputStream = new FileInputStream(file);
        while (true) {
            byte[] buffer = new byte[1024];
            //尽可能能的一次读1024个字节，不够则读多少就是多少，返回读取到的长度
            int len = inputStream.read(buffer);
            if(len == -1) {
                break;
            }
            System.out.println(len);

        }
        inputStream.close();

    }
}
