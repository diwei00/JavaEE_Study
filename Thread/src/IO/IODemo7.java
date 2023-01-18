package IO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//扫描指定目录，并找到名称或者内容中包含指定字符的所有普通文件（不包含目录）
public class IODemo7 {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("请输入你要扫描的目录路径");
        String rootPath = scanner.nextLine();
        File rootFile = new File(rootPath);
        if(!rootFile.isDirectory()) {
            System.out.println("输入错误");
            return;
        }
        System.out.println("请输入指定字符");
        String Deletename = scanner.next();
        List<File> list = new ArrayList<>();
        func(rootFile, Deletename, list);

        //打印路径
        for(int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            System.out.println(file.getPath());
        }

    }

    private static void func(File rootFile, String deletename, List<File> list) {
        File[] files = rootFile.listFiles();
        if(files == null) {
            return;
        }
        for (File f : files) {
            //目录就递归
            if(f.isDirectory()) {
                func(f, deletename, list);
            }else {
                //名字一样
                if(f.getName().contains(deletename)) {
                    list.add(f);
                }
                //内容一样
                try(Reader reader = new FileReader(f)) {
                    //循环读取
                    while (true) {
                        int tmp = reader.read();
                        if(tmp == -1) {
                            break;
                        }
                        if(tmp == deletename.charAt(0)) {
                            list.add(f);
                            break;
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
