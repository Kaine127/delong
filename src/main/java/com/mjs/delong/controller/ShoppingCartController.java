package com.mjs.delong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mjs.delong.common.BaseContext;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.ShoppingCart;
import com.mjs.delong.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据为:{}",shoppingCart);

        //获取线程中的id
        Long currentId = BaseContext.getCurrentId();
        //设置UserId
        shoppingCart.setUserId(currentId);

        //获取DishId
        Long dishId = shoppingCart.getDishId();

        //建立条件构造器
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);

        //根据菜品还是套餐id进行操作
        if (dishId != null){
            //菜品id的情况
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //套餐id的情况
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查看是否能查出购物车数据
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lambdaQueryWrapper);

        if (cartServiceOne != null){
            //有相关数据,加一然后更新
            cartServiceOne.setNumber(cartServiceOne.getNumber()+1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            //没有相关数据,设置默认数量为1 , 然后更新
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            //将返回值统一一下
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);


    }

    /**
     * 获取购物车列表
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车....");

        //获取当前用户的id
        Long currentId = BaseContext.getCurrentId();
        //建立条件构造器
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,currentId);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(lambdaQueryWrapper);

        return R.success("清除购物车成功");
    }


}
