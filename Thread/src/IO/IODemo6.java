package IO;

import java.io.*;
import java.util.Scanner;



public class IODemo6 {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("输入你要拷贝文件路径");
        String srcPath = scanner.next();
        File srcFile = new File(srcPath);
        //如果不是文件
        if(!srcFile.isFile()) {
            System.out.println("输入错误");
            return;
        }
        System.out.println("输入拷贝目的地路径");
        String destPath = scanner.next();
        File destFile = new File(destPath);
        //如果拷贝目的文件已存在
        if(destFile.isFile()) {
            System.out.println("输入错误");
            return;
        }
        copyFiles(srcFile, destFile);
    }

    private static void copyFiles(File srcFile, File destFile) {
        //边读边拷贝
        //读源头文件，写目的地文件
        try(InputStream inputStream = new FileInputStream(srcFile);
            OutputStream outputStream = new FileOutputStream(destFile)) {

            //边读边写
            while (true) {
                byte[] buffer = new byte[1024];
                int len = inputStream.read(buffer);
                if(len == -1) {
                    break;
                }
                outputStream.write(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
