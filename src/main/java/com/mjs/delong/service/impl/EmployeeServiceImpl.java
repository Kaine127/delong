package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.mapper.EmployeeMapper;
import com.mjs.delong.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
