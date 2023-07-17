package com.example.demo.mapper;

import com.example.demo.entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    int add(Log log);

}
