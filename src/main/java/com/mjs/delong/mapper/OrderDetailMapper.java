package com.mjs.delong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mjs.delong.entity.OrderDetail;
import com.mjs.delong.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
