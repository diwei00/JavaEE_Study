package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.ServiceException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileParamsDTO;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileResultVO;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description TODO
 */
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFilesMapper mediaFilesMapper;

    @Autowired
    private MinioClient minioClient;

    // 普通文件桶
    @Value("${minio.bucket.files}")
    private String bucket_files;

    // 代理对象
    @Autowired
    private MediaFileService currentProxy;

    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDTO queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }


    // 获取文件默认存储目录路径 年/月/日
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/") + "/";
        return folder;
    }

    // 获取文件的 md5
    private String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5Hex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取MimeType
    private String getMimeType(String extension) {
        if (extension == null)
            extension = "";
        // 根据扩展名取出 mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        // 通用 mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    public boolean addMediaFilesToMinIO(String localFilePath, String mimeType,
                                        String bucket, String objectName) {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(localFilePath)
                    .contentType(mimeType)
                    .build();
            minioClient.uploadObject(testbucket);
            log.debug("上传文件到 minio 成功,bucket:{},objectName:{}", bucket, objectName);
            System.out.println("上传成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件到 minio 出错,bucket:{},objectName:{},错误原因:{} ",
                    bucket, objectName, e.getMessage(), e);
//            throw new ServiceException("上传文件到文件系统失败");
        }
        return false;
    }

    /**
     * @param companyId           机构 id
     * @param fileMd5             文件 md5 值
     * @param uploadFileParamsDto 上传文件的信息
     * @param bucket              桶
     * @param objectName          对象名称
     * @return com.xuecheng.media.model.po.MediaFiles
     * @description 将文件信息添加到文件表
     */
    @Transactional
    public MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDTO uploadFileParamsDto,
                                        String bucket, String objectName) {
        // 从数据库查询文件
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        // 防止重复入库
        if (mediaFiles == null) {
            mediaFiles = new MediaFiles();
            // 拷贝基本信息
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            mediaFiles.setId(fileMd5);
            mediaFiles.setFileId(fileMd5);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setUrl("/" + bucket + "/" + objectName);
            mediaFiles.setBucket(bucket);
            mediaFiles.setFilePath(objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setAuditStatus("002003");
            mediaFiles.setStatus("1");
            // 保存文件信息到文件表
            int result = mediaFilesMapper.insert(mediaFiles);
            if (result < 0) {
                log.error("保存文件信息到数据库失败,{}", mediaFiles.toString());
                throw new ServiceException("保存文件信息失败");
            }
            log.debug("保存文件信息到数据库成功,{}", mediaFiles.toString());
        }
        return mediaFiles;
    }

    /**
     * 注意：
     *      非事务方法调用事务方法，事务不生效。
     * 原因：
     *      非事务方法调用不是代理对象（只有是代理对象调用且加@Transactional注解事务才会生效）
     * 解决方案：
     *      通过代理对象调用，提升非事务方法为接口，通过接口调用
     * @param companyId
     * @param uploadFileParamsDto
     * @param localFilePath
     * @return
     */
    @Override
    public UploadFileResultVO uploadFile(Long companyId, UploadFileParamsDTO uploadFileParamsDto, String localFilePath) {

        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new ServiceException("文件不存在");
        }
        // 文件名称
        String filename = uploadFileParamsDto.getFilename();
        // 文件扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        // 文件 mimeType
        String mimeType = getMimeType(extension);
        // 文件的 md5 值
        String fileMd5 = getFileMd5(file);
        // 文件的默认目录
        String defaultFolderPath = getDefaultFolderPath();
        // 存储到 minio 中的对象名(带目录)
        String objectName = defaultFolderPath + fileMd5 + extension;
        // 将文件上传到 minio
        boolean b = addMediaFilesToMinIO(localFilePath, mimeType, bucket_files, objectName);
        // 文件大小
        uploadFileParamsDto.setFileSize(file.length());
        // 将文件信息存储到数据库
        // 注意：保证事务生效
        MediaFiles mediaFiles = currentProxy.addMediaFilesToDb(companyId, fileMd5,
                uploadFileParamsDto, bucket_files, objectName);
        // 返回数据
        UploadFileResultVO uploadFileResultVO = new UploadFileResultVO();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultVO);
        return uploadFileResultVO;

    }


}
