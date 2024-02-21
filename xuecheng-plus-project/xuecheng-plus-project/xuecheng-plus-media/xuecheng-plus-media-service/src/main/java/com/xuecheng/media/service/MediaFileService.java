package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileParamsDTO;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 媒资文件管理业务类
 */
public interface MediaFileService {

    /**
     * 媒资文件查询
     *
     * @param companyId
     * @param pageParams
     * @param queryMediaParamsDto
     * @return
     */
    PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDTO queryMediaParamsDto);


    UploadFileResultVO uploadFile(Long companyId,
                                  UploadFileParamsDTO uploadFileParamsDto, String localFilePath);


    MediaFiles addMediaFilesToDb(Long companyId, String
            fileMd5, UploadFileParamsDTO uploadFileParamsDto, String bucket, String objectName);

    // 检查文件是否存在
    RestResponse<Boolean> checkFile(String fileMd5);

    // 检查分块是否存在
    RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex);

    // 上传分块
    RestResponse uploadChunk(String fileMd5, int chunk, MultipartFile file);

    // 合并分块
    RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal,
                             UploadFileParamsDTO uploadFileParamsDto);

    File downloadFileFromMinIO(String bucket, String objectName);

    boolean addMediaFilesToMinIO(String localFilePath, String mimeType, String bucket, String objectName);


    MediaFiles getFileById(String mediaId);
}
