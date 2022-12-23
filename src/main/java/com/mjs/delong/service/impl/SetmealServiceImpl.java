package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.common.CustomException;
import com.mjs.delong.entity.Dish;
import com.mjs.delong.entity.Setmeal;
import com.mjs.delong.entity.SetmealDish;
import com.mjs.delong.entity.SetmealDto;
import com.mjs.delong.mapper.DishMapper;
import com.mjs.delong.mapper.SetmealMapper;
import com.mjs.delong.service.DishService;
import com.mjs.delong.service.SetmealDishService;
import com.mjs.delong.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 保存套餐信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);

        //拿出套餐和菜品的连接信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //setmealDish表中需要setmeal的id
        Long id = setmealDto.getId();
        //设置到每一个setmealDish里
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 根据id删除套餐和套餐关联菜品的信息
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //先判断是否能删除套餐 正在起售的无法删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(Setmeal::getId,ids);
        lambdaQueryWrapper1.eq(Setmeal::getStatus,1);
        int count = this.count(lambdaQueryWrapper1);

        //如果有的话 , 说明有起售的套餐, 不能删除
        if (count > 0){
            throw new CustomException("删除的套餐中有在售的套餐, 不能删除");
        }
        //没有在售套餐, 根据id删除套餐信息
        this.removeByIds(ids);

        //根据SetmealId删除套餐关联菜品的信息
        //建立条件构造器
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据 ----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper2);
    }
}
