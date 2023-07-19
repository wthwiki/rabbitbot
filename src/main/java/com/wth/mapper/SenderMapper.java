package com.wth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wth.entity.dto.BaseSender;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SenderMapper extends BaseMapper<BaseSender> {
}
