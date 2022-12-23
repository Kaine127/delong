package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.entity.ShoppingCart;
import com.mjs.delong.mapper.EmployeeMapper;
import com.mjs.delong.mapper.ShoppingCartMapper;
import com.mjs.delong.service.EmployeeService;
import com.mjs.delong.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
