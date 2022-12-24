package com.mjs.delong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.Category;
import com.mjs.delong.entity.Dish;
import com.mjs.delong.entity.DishDto;
import com.mjs.delong.entity.DishFlavor;
import com.mjs.delong.service.CategoryService;
import com.mjs.delong.service.DishFlavorService;
import com.mjs.delong.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info(dishDto.toString());

        dishService.saveDishWithFlavor(dishDto);
        //清理某个分类下的菜品缓冲数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);
        //获取分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);

        //获取条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加模糊查询条件
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        //添加更新时间排序
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        //查询
        dishService.page(pageInfo, lambdaQueryWrapper);
        //获取结果集
        List<Dish> records = pageInfo.getRecords();
        //获取categoryId集合,用Set集合避免重复
        Set<Long> categotyIds = records.stream().map((dish -> dish.getCategoryId())).collect(Collectors.toSet());
        //根据集合获取category集合
        List<Category> categories = categoryService.listByIds(categotyIds);
        //获取categoryId和categoryName
        Map<Long, String> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        //遍历Dish结果集, 向里面添加对应的categoryName
        for (Dish dish : records) {
            //查询categoryMap的id获取categoryName
            dish.setCategoryName(categoryMap.get(dish.getCategoryId()));
        }
        //将records返回回去
        pageInfo.setRecords(records);

        return R.success(pageInfo);


    }

    /**
     * 根据id进行菜品查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        log.info("查询菜品的id:{}",id);
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateDishWithFlavor(dishDto);
        //这里删除所有的菜品信息, 因为修改时可能会修改分类.
        String key = "dish_*";
        redisTemplate.delete(key);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList = null;

        //动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //先从redis中获取缓存数据
        dishDtoList = (List<DishDto>)redisTemplate.opsForValue().get(key);
        if (dishDtoList != null){
            //如果存在 , 直接返回 , 无需查证
            return R.success(dishDtoList);
        }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = dishService.list(queryWrapper);

        List<Long> dishIds = dishList.stream().map(dish1 -> dish1.getId()).collect(Collectors.toList());


        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<DishFlavor>();
        lambdaQueryWrapper.in(DishFlavor::getDishId,dishIds);
        //SQL:select * from dish_flavor where dish_id in ('','');

        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);

        dishDtoList = new ArrayList<DishDto>();
        for (Dish dish2 : dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish2,dishDto);
            List<DishFlavor> dishFlavorList1 = dishFlavorList.stream().filter(dishFlavor -> dishFlavor.equals(dish2.getId())).collect(Collectors.toList());
            dishDto.setFlavors(dishFlavorList1);
            dishDtoList.add(dishDto);
        }

        //如果不存在 , 需要查询数据库, 将查询到的菜品数据缓存到redis中
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);


    }
}
