package com.example.poidemo.service.impl;

import com.example.poidemo.entity.User;
import com.example.poidemo.mapper.UserMapper;
import com.example.poidemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

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

    @Override
    public Boolean getUserToExcel(String ids, HttpServletResponse response) {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(ids.split(",")));
        if(CollectionUtils.isEmpty(users)) {
            log.info("用户不存在，ids={}", ids);
            return false;
        }
        exportExcel("templates/userTemplate.xlsx", response, users);


        return true;
    }

    private void exportExcel(String templatePath, HttpServletResponse response, List<User> users) {
        ClassPathResource resource = new ClassPathResource(templatePath);
        try(
                InputStream fis = resource.getInputStream();
                // 创建excel文档对象
                XSSFWorkbook workbook = (XSSFWorkbook)WorkbookFactory.create(fis);
        ) {
            // 获取第一个Sheet
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 复制sheet
//            XSSFSheet newSheet = workbook.cloneSheet(0);
            workbook.setSheetName(workbook.getSheetIndex(sheet), "用户信息");
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                // 创建行
                XSSFRow row = sheet.createRow(i + 1);
                for(int j = 0; j < 5; j++) {
                    switch (j){
                        case 0:
                            // 创建单元格并且设置value
                            row.createCell(j).setCellValue(user.getUsername());
                            break;
                        case 1:
                            row.createCell(j).setCellValue(user.getAge());
                            break;
                        case 2:
                            row.createCell(j).setCellValue(user.getSex());
                            break;
                        case 3:
                            row.createCell(j).setCellValue(user.getHeight());
                            break;
                        case 4:
                            row.createCell(j).setCellValue(user.getWeight());
                            break;
                    }
                }

            }

            // 下载excel
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=user.xlsx");
            response.setCharacterEncoding("UTF-8");
            workbook.write(response.getOutputStream());

        }catch (IOException e) {
            log.info("获取模板异常！");
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            log.info("模板文件异常！");
            e.printStackTrace();
        }



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
//            // 将文档转换为字节数组输出流
//            baos = new ByteArrayOutputStream();
//            document.write(baos);
//            byte[] bytes = baos.toByteArray();

            response.setCharacterEncoding("utf-8"); //设置编码格式
            response.setHeader("Content-Disposition", "attachment; filename=\"userTemplate.docx\"");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            document.write(response.getOutputStream());
//            response.setContentLength(bytes.length);
//
//            // 将字节数组写入HTTP响应
//            outputStream = response.getOutputStream();
//            outputStream.write(bytes);
//            // 刷新输出流缓冲区
//            outputStream.flush();


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
