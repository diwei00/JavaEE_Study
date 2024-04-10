import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @Description
 * @Author wh
 * @Date 2024/4/10 9:42
 */
public class ModifyIp {

    public static void readFile() {
        Scanner scanner = new Scanner(System.in);
        String path = "";
        String newIp = "";
        System.out.println("请输入指定目录：");
        path = scanner.nextLine();
        System.out.println("请输入新IP地址：");
        newIp = scanner.nextLine();
        Pattern pattern = Pattern.compile("(^((2[0-4]\\d.)|(25[0-5].)|(1\\d{2}.)|(\\d{1,2}.))((2[0-5]{2}.)|(1\\d{2}.)|(\\d{1,2}.){2})((1\\d{2})|(2[0-5]{2})|(\\d{1,2})))");
        Matcher matcher = pattern.matcher(newIp);
        if(!matcher.find()) {
            System.out.println("输入IP地址有误！");
            return;
        }

        // 读取文件
        try {
            // 读取到所有路径流
            String finalNewIp = newIp;
            Stream<Path> fileList = Files.walk(Paths.get(path));
            fileList
                    .filter(item -> Files.isDirectory(item))
                    .forEach(item -> {
                        // 这里得到存在文件的目录
                        File file = new File(item.toUri());
                        // 拿到该目录下所有子目录
                        File[] files = file.listFiles();
                        // 过滤非文件子目录
                        Arrays.stream(files).filter(t -> t.isFile()).forEach(t -> {
                            // 拿到子目录文件地址
                            String filePath = t.getPath();
                            // 替换IP
                            replaceIP(filePath, finalNewIp);
                        });
                    });


        } catch (IOException e) {
            System.out.println("'error: 路径不存在/该路径为文件");
            e.printStackTrace();
        }


    }

    // 替换IP
    private static void replaceIP(String filePath, String newIp) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        File tempFile = null;
        try {
            String finalNewIp = newIp;
            // 读源文件
            reader = new BufferedReader(new FileReader(filePath));
            // 创建临时文件
            tempFile = File.createTempFile("tmp", ".txt");
            // 追加写文件
            writer = new BufferedWriter(new FileWriter(tempFile.getPath(), true));

            String line;
            // 提取IP地址
            Pattern pattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
            while ((line = reader.readLine()) != null) {
                // 进行匹配
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    // 找到旧IP
                    String oldIP = matcher.group();
                    line = line.replace(oldIP, finalNewIp);
                    System.out.println("将旧IP：" + oldIP + "替换为：" + finalNewIp + ". 文件路径：" + filePath);
                }
                // 写入临时文件
                writer.write(line + "\n");
                // 切记：刷新缓冲区
                writer.flush();

            }
            // 使用临时文件替换旧文件
            OutputStream outputStream = new FileOutputStream(new File(filePath));
            Files.copy(Paths.get(tempFile.getPath()), outputStream);

        } catch (FileNotFoundException e) {
            System.out.println("文件路径有误");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("临时文件创建失败");
            e.printStackTrace();
        }finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                System.out.println("流关闭失败");
                e.printStackTrace();
            }
        }
        // E:\tmp\bbb
        // 192.168.2.20
        // 手动127.0.0.1的成分
        // /home/wh/tmp
    }


}
