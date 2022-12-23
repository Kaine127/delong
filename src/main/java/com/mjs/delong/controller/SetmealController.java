package com.mjs.delong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.Category;
import com.mjs.delong.entity.Setmeal;
import com.mjs.delong.entity.SetmealDto;
import com.mjs.delong.service.CategoryService;
import com.mjs.delong.service.SetmealDishService;
import com.mjs.delong.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构建分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        //建立条件构造器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加模糊查询条件
        lambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, lambdaQueryWrapper);

        //获取setmeal结果集
        List<Setmeal> records = pageInfo.getRecords();
        Set<Long> categoryIdSet = records.stream().map(setmeal -> setmeal.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类对象
        List<Category> categories = categoryService.listByIds(categoryIdSet);

        //获取categoryId和categoryName
        Map<Long, String> map = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        //遍历设置categoryName
        for (Setmeal record : records) {
            //根据categoryId设置categoryName
            record.setCategoryName(map.get(record.getCategoryId()));
        }
        //将records也设置回去
        pageInfo.setRecords(records);

        return R.success(pageInfo);

    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids={}",ids);
        setmealService.deleteWithDish(ids);
        return R.success("套餐删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(@RequestBody Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
