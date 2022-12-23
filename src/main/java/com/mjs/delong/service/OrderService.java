package com.mjs.delong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.entity.Orders;

public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);
}
