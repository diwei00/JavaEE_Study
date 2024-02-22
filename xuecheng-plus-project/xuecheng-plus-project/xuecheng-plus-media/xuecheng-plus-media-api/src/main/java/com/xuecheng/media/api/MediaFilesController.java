package com.xuecheng.media.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileParamsDTO;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileResultVO;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 媒资文件管理接口
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
public class MediaFilesController {


    @Autowired
    private MediaFileService mediaFileService;


    @ApiOperation("媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody(required = false) QueryMediaParamsDTO queryMediaParamsDto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiels(companyId, pageParams, queryMediaParamsDto);
    }

    @ApiOperation("上传文件")
    @RequestMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultVO upload(@RequestPart("filedata") MultipartFile filedata,
                                     @RequestParam(value = "folder", required = false) String folder,
                                     @RequestParam(value = "objectName", required = false) String objectName) throws IOException {
        Long companyId = 1232141425L;
        UploadFileParamsDTO uploadFileParamsDto = new UploadFileParamsDTO();
        // 文件大小
        uploadFileParamsDto.setFileSize(filedata.getSize());
        // 图片
        uploadFileParamsDto.setFileType("001001");
        // 文件名称
        uploadFileParamsDto.setFilename(filedata.getOriginalFilename());
        // 文件大小
        long fileSize = filedata.getSize();
        uploadFileParamsDto.setFileSize(fileSize);
        // 创建临时文件（实现拿去文件本地路径）
        File tempFile = File.createTempFile("minio", "temp");
        // 上传的文件拷贝到临时文件
        filedata.transferTo(tempFile);
        // 本地文件路径
        String absolutePath = tempFile.getAbsolutePath();
        // 上传文件
        UploadFileResultVO uploadFileResultDto = mediaFileService.
                uploadFile(companyId, uploadFileParamsDto, absolutePath, null);
        return uploadFileResultDto;
    }

}
