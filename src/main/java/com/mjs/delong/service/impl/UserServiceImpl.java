package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.SetmealDish;
import com.mjs.delong.entity.User;
import com.mjs.delong.mapper.SetmealDishMapper;
import com.mjs.delong.mapper.UserMapper;
import com.mjs.delong.service.SetmealDishService;
import com.mjs.delong.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
