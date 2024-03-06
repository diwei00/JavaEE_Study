package com.xuechen.minio;


import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class MinioTest {

    // api端口
    static MinioClient minioClient = MinioClient.builder()
            .endpoint("http://192.168.101.128:9090")
            .credentials("minioadmin", "minioadmin")
            .build();

    /**
     * 上传文件测试
     */
    @Test
    public void upload() {
        // 根据扩展名取出 mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".png");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用 mimeType，字节流
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        System.out.println(extensionMatch.getMimeType());
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("test") // 指定桶
                    // .object("test001.mp4")
                    .object("001/1.png") // 添加子目录
                    .filename("C:\\Users\\29456\\Desktop\\2.png")
                    .contentType(mimeType) // 默认根据扩展名确定文件内容 类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }
    }

    /**
     * 删除文件测试
     */
    @Test
    public void delete() {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().
                            bucket("test").object("001/1.png").build());
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }

    }

    /**
     * 查询文件
     */
    @Test
    public void getFile() throws IOException {
        GetObjectArgs getObjectArgs =
                GetObjectArgs.builder().bucket("test").object("1.png").build();
        try (
                // minio读取到文件，返回文件流
                FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                FileOutputStream outputStream = new FileOutputStream(new File("E:\\tmp\\1a.png"));
        ) {
            // 拷贝文件到本地
            IOUtils.copy(inputStream, outputStream);


        } catch (Exception e) {
            e.printStackTrace();
        }


        // 校验文件的完整性对文件的内容进行 md5
        // 第一次下载文件
        FileInputStream fileInputStream1 = new FileInputStream(new File("E:\\tmp\\1.png"));
        String source_md5 = DigestUtils.md5Hex(fileInputStream1);
        // 第二次下载文件
        FileInputStream fileInputStream = new FileInputStream(new File("E:\\tmp\\1a.png"));
        String local_md5 = DigestUtils.md5Hex(fileInputStream);
        // 比对MD5值
        if (source_md5.equals(local_md5)) {
            System.out.println("下载成功");
        }
    }

    /**
     * 将分块文件上传至 minio
     */
    @Test
    public void uploadChunk() {
        String chunkFolderPath = "E:\\video\\test\\chunk\\";
        File chunkFolder = new File(chunkFolderPath);
        // 分块文件
        File[] files = chunkFolder.listFiles();
        // 将分块文件上传至 minio
        for (int i = 0; i < files.length; i++) {
            try {
                UploadObjectArgs uploadObjectArgs =
                        UploadObjectArgs.builder()
                                .bucket("test")
                                .object("chunk/" + i)
                                .filename(files[i].getAbsolutePath())
                                .build();
                minioClient.uploadObject(uploadObjectArgs);
                System.out.println("上传分块成功" + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并文件，要求分块文件最小 5M
     *
     * @throws Exception
     */
    @Test
    public void testMerge() throws Exception {
        // 原数据
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(5)
                .map(i -> ComposeSource.builder()
                        .bucket("test")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());
        // 合并数据
        ComposeObjectArgs composeObjectArgs =
                ComposeObjectArgs
                        .builder()
                        .bucket("test")
                        .object("merge01.mp4 ")
                        .sources(sources)
                        .build();
        minioClient.composeObject(composeObjectArgs);
    }

    /**
     * 清除分块文件
     */
    @Test
    public void testRemoveObjects() {
        // 合并分块完成将分块文件清除
        // 要删除的文件
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                .limit(5)
                .map(i -> new
                        DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());
        //  删除文件
        RemoveObjectsArgs removeObjectsArgs =
                RemoveObjectsArgs.builder()
                        .bucket("test")
                        .objects(deleteObjects)
                        .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        // 遍历后才会真正删除
        results.forEach(r -> {
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
