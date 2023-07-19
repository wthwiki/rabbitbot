package com.wth.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wth.entity.dto.BaseMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface MessageMapper extends BaseMapper<BaseMessage> {
}
