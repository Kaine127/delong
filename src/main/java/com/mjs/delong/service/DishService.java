package com.mjs.delong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mjs.delong.entity.Category;
import com.mjs.delong.entity.Dish;
import com.mjs.delong.entity.DishDto;
import com.mjs.delong.entity.SetmealDto;

public interface DishService extends IService<Dish> {

    public void saveDishWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateDishWithFlavor(DishDto dishDto);


}
