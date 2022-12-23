package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.Setmeal;
import com.mjs.delong.entity.SetmealDish;
import com.mjs.delong.mapper.SetmealDishMapper;
import com.mjs.delong.mapper.SetmealMapper;
import com.mjs.delong.service.SetmealDishService;
import com.mjs.delong.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
