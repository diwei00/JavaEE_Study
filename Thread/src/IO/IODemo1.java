package IO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class IODemo1 {
    public static void main(String[] args) throws IOException {
        //相对路径，file对象就指向这个文件
        File file = new File("./test.txt");
        //获得上级目录
        System.out.println(file.getParent());
        //获得文件名
        System.out.println(file.getName());
        //获得文件相对路径
        System.out.println(file.getPath());
        //获得文件全路径（拼接）
        System.out.println(file.getAbsolutePath());
        //或的文件全路径
        System.out.println(file.getCanonicalPath());
        //是否为目录
        System.out.println(file.isDirectory());
        //是否为文件
        System.out.println(file.isFile());
        //文件长度
        System.out.println(file.length());
        //是否支持写功能
        System.out.println(file.canWrite());
        //是否支持读功能
        System.out.println(file.canRead());
        //是否可以执行
        System.out.println(file.canExecute());
        //是否存在这个文件
        System.out.println(file.exists());
        //是否为隐藏文件
        System.out.println(file.isHidden());
        //返回file对象代表的目录下所有文件名，如果代表文件则返回null
        String[] tmp = file.list();
        System.out.println(Arrays.toString(tmp));
        //返回file对象代表的目录下所有文件，以file对象表示
        File[] tmp2 = file.listFiles();
        System.out.println(tmp2.length);


    }

}
