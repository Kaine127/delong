package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.common.CustomException;
import com.mjs.delong.entity.Category;
import com.mjs.delong.entity.Dish;
import com.mjs.delong.entity.Setmeal;
import com.mjs.delong.mapper.CategoryMapper;
import com.mjs.delong.service.CategoryService;
import com.mjs.delong.service.DishService;
import com.mjs.delong.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;


    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {
        //检查菜品
        //创建条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapperDish = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapperDish.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(lambdaQueryWrapperDish);
        //如果已经关联,抛出一个业务异常
        if (dishCount!=0){
            throw new CustomException("该分类存在菜品信息,无法删除");
        }
        //检查套餐
        //创建条件构造器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapperSetmeal = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapperSetmeal.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(lambdaQueryWrapperSetmeal);
        //如果已经关联,抛出一个业务异常
        if (setmealCount!=0){
            throw new CustomException("该分类存在套餐信息,无法删除");
        }

        //正常删除
        super.removeById(id);

    }
}
