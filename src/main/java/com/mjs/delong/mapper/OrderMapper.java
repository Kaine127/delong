package com.mjs.delong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
