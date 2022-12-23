package com.mjs.delong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.Category;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> saveCategory(@RequestBody Category category){
        log.info("新增分类: {}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //创建分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        //建立mybatis-plus的条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //添加排序语句,根据sort排序
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        //查询
        categoryService.page(pageInfo,lambdaQueryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 分类删除
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteById(Long id){
        log.info("删除分类,其id为 {}",id);
        categoryService.remove(id);
        return R.success("分类删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息:{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 按类型获取分类信息
     * @param
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info("获取分类,其类型为: {}",category.getType());
        //建立条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //设置条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

        //设置排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);

        System.out.println("返回分类列表");
        return R.success(list);
    }




}
