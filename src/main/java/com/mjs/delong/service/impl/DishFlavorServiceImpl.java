package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.DishFlavor;
import com.mjs.delong.mapper.DishFlavorMapper;
import com.mjs.delong.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
