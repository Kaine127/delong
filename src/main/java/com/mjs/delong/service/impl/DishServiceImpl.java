package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.Dish;
import com.mjs.delong.entity.DishDto;
import com.mjs.delong.entity.DishFlavor;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.mapper.DishMapper;
import com.mjs.delong.mapper.EmployeeMapper;
import com.mjs.delong.service.DishFlavorService;
import com.mjs.delong.service.DishService;
import com.mjs.delong.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService  dishFlavorService;

    /**
     *保存菜品以及口味信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto) {
        //先存储Dish数据
        this.save(dishDto);

        //再取出DishFlavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        //获取DishId
        Long id = dishDto.getId();

        //向每一个DishFlavor的个体里面加入DishId
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品和口味
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        //将菜品信息复制到dto中
        BeanUtils.copyProperties(dish,dishDto);

        //创建构造语句
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);

        //将口味封装进dto
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    /**
     * 更新菜品以及口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateDishWithFlavor(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);

        //删除原始的菜品口味
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

        //获得新的口味信息
        //取出DishFlavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        //获取DishId
        Long id = dishDto.getId();

        //向每一个DishFlavor的个体里面加入DishId
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

}
