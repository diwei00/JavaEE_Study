package IO;

import java.io.File;
import java.util.Scanner;
//扫描目录。删除所有指定文件
public class IODemo5 {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("请输入你要扫描目录的路径：");
        String path = scanner.next();
        File file = new File(path);
        if(!file.isDirectory()) {
            System.out.println("输入错误");
            return;
        }
        System.out.println("请输入你要删除的文件的字符");
        String str = scanner.next();
        fun(file, str);

    }

    private static void fun(File root, String str) {
        //获得该目录下所有文件包括目录对象
        File[] files = root.listFiles();
        //递归结束
        if(files == null) {
            return;
        }
        for(File file : files) {
            //是目录就递归
            if(file.isDirectory()) {
                fun(file, str);
            }else {
                //删除文件
                String name = file.getName();
                if(name.contains(str)) {
                    file.delete();
                }
            }
        }
    }
}
