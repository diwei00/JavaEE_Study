package com.xuecheng.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {


    /**
     * 查询待处理任务
     * @param shardTotal 分片总数
     * @param shardIndex 分片标识
     * @param count 查询记录数
     * @return
     */
    @Select("select * from media_process t where t.id % #{shardTotal} = #{shardIndex} and (t.status = '1' or t.status = '3') and t.fail_count < 3 limit #{count}")
    List<MediaProcess> selectListByShardIndex(@Param("shardTotal")
                                              int shardTotal, @Param("shardIndex") int shardIndex, @Param("count")
                                              int count);

    /**
     * 开启一个任务
     * @param id 任务 id
     * @return 更新记录数
     *
     * 分布式锁的实现（乐观锁）多个机器修改同一条字段，只有一个机器会修改成功（获取到锁）
     * 防止任务重复消费
     * 场景：
     *      多个执行器查询同一张表，并且涉及修改，存在分布式系统安全问题
     */
    @Update("update media_process m set m.status='4' where(m.status='1' or m.status='3') and m.fail_count<3 and m.id=#{id}")
    int startTask(@Param("id") long id);

}
