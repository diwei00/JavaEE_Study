package IO;

import java.io.File;
import java.io.IOException;

public class IODemo2 {
    public static void main(String[] args) throws IOException {
        File file = new File("./test.txt");
        File tmp = new File("./test3.txt");
        //修改文件名
        //System.out.println(file.renameTo(tmp));
        //创建目录(file对象所指向的目录)
        System.out.println(file.mkdir());
        File file1 = new File("./test/test2/test3");
        //创建目录(file对象所指向的一连串目录)
        System.out.println(file1.mkdirs());
        //删除file对象所指向的目录
        System.out.println(tmp.delete());
        //程序退出时，删掉file对象所指向的文件(临时文件)
        tmp.deleteOnExit();

        File file2 = new File("./test2.txt");
        //创建一个file所指向的文件
        System.out.println(file2.createNewFile());
        System.out.println(file2.delete());

    }

}
