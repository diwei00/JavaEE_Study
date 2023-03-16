package IO;

import java.io.*;
import java.util.Scanner;

public class IODEmo8 {
    public static void main(String[] args) throws IOException {
        File file = new File("E:/tmp/test.txt");
        try(InputStream inputStream = new FileInputStream(file)) {
            //将指定流对象写入Scanner参数中
            Scanner scanner = new Scanner(inputStream);
            //读取数据
            while (scanner.hasNext()) {
                String tmp = scanner.next();
                System.out.println(tmp);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
