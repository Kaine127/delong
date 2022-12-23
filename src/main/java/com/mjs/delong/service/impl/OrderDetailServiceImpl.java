package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.OrderDetail;
import com.mjs.delong.entity.Orders;
import com.mjs.delong.mapper.OrderDetailMapper;
import com.mjs.delong.mapper.OrderMapper;
import com.mjs.delong.service.OrderDetailService;
import com.mjs.delong.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
