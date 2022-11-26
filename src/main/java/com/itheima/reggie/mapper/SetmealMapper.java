package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.itheima.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 金宗文
 * @version 1.0
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
