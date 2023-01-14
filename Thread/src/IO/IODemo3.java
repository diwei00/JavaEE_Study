package IO;

import java.io.*;
import java.util.Arrays;

//字节流（以字节为单位）操作二进制数据
public class IODemo3 {
    public static void main(String[] args) throws IOException {
        File file = new File("./test.txt");
        //创建操作这个文件的流对象，并且打开这个文件
        //可以用file对象，或者路径的方式构造流对象
        InputStream inputStream = new FileInputStream(file);
        while (true) {
            //读文件流
            //不带参数以字节为单位，读取后返回
            //带数组参数，读取数据存入数组中
            int tmp = inputStream.read();
            //读到文件结尾返回-1
            if(tmp == -1) {
                break;
            }
            System.out.printf("%x\n", tmp);
        }
        inputStream.close();
    }
}

