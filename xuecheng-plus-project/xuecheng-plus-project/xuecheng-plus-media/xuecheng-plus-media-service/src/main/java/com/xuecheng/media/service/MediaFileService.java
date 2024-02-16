package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileParamsDTO;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileResultVO;

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


    MediaFiles addMediaFilesToDb(Long companyId,String
            fileMd5,UploadFileParamsDTO uploadFileParamsDto,String bucket,String objectName);


}
