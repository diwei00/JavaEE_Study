package com.example.poidemo.service.impl;

import com.example.poidemo.entity.User;
import com.example.poidemo.mapper.UserMapper;
import com.example.poidemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author wh
 * @Date 2024/5/16 19:58
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUserToWord(String id, HttpServletResponse response) {
        User user = userMapper.selectById(id);
        if(user == null) {
            log.info("用户不存在，id={}", id);
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("{{username}}", user.getUsername());
        map.put("{{height}}", String.valueOf(user.getHeight()));
        map.put("{{sex}}", user.getSex());
        map.put("{{weight}}", String.valueOf(user.getWeight()));
        map.put("{{age}}", String.valueOf(user.getAge()));
        exportWord("templates/userTemplate.docx", response, map);
        return "export word!";
    }



    private void exportWord(String templatePath, HttpServletResponse response, Map<String, String> replacements)  {
        // 读取resources下的模板文件
        ClassPathResource resource = new ClassPathResource(templatePath);
        OutputStream outputStream = null;
        ByteArrayOutputStream baos = null;
        try (
                InputStream fis = resource.getInputStream();
                // 创建Word文档对象
                XWPFDocument document = new XWPFDocument(fis);
        ) {

            // 遍历段落中的文本，查找并替换标记
            for (XWPFParagraph p : document.getParagraphs()) {
                replaceTextInParagraph(p, replacements);
            }
//            // 遍历表格中的文本，查找并替换标记
//            for (XWPFTable table : document.getTables()) {
//                for (XWPFTableRow row : table.getRows()) {
//                    for (XWPFTableCell cell : row.getTableCells()) {
//                        for (XWPFParagraph para : cell.getParagraphs()) {
//                            replaceTextInParagraph(para, replacements);
//                        }
//                    }
//                }
//            }

            // 导出文档
            // 将文档转换为字节数组输出流
            baos = new ByteArrayOutputStream();
            document.write(baos);
            byte[] bytes = baos.toByteArray();

            response.setCharacterEncoding("utf-8"); //设置编码格式
            response.setHeader("Content-Disposition", "attachment; filename=\"userTemplate.docx\"");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLength(bytes.length);

            // 将字节数组写入HTTP响应
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
            // 刷新输出流缓冲区
            outputStream.flush();


//            document.write(response.getOutputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
                if(baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("关闭输出流失败");
            }

        }
    }



    private void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        // XWPFRun：表示段落中连续的格式化文本，一个段落有多个run，因为段落中的文本可能包含多个不同的格式
        List<XWPFRun> runs = paragraph.getRuns();
        if (!runs.isEmpty()) {
            StringBuilder text = new StringBuilder();
            // 拼接段落中所有run
            for (XWPFRun r : runs) {
                text.append(r.getText(0));
            }
            String replacedText = text.toString();

            // 遍历替换标记，存在则替换
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                replacedText = replacedText.replace(entry.getKey(), entry.getValue());
            }
            // 清除段落原有内容
            int runSize = runs.size();
            for(int i = 0; i < runSize; i++) {
                paragraph.removeRun(0);

            }
            // 添加替换后的内容
            paragraph.createRun().setText(replacedText);
        }
    }


}
