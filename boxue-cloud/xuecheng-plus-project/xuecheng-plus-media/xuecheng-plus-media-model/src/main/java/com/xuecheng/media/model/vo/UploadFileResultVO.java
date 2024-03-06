package com.xuecheng.media.model.vo;

import com.xuecheng.media.model.po.MediaFiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 上传文件数据返回对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UploadFileResultVO extends MediaFiles {
}
