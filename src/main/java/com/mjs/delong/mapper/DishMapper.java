package com.mjs.delong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mjs.delong.entity.Dish;
import com.mjs.delong.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
