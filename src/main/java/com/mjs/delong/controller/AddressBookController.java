package com.mjs.delong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mjs.delong.common.BaseContext;
import com.mjs.delong.common.CustomException;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.AddressBook;
import com.mjs.delong.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        log.info("addressBook:{}",addressBook);
        //建立条件构造器
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        log.info("addressBook:{}",addressBook);
        //建立更新语句
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        //设置当前用户的默认地址
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        //将是否默认设置为0,这里是全部的都设置为0
        wrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(wrapper);

        //设置传过来的地址为1, 即默认地址
        addressBook.setIsDefault(1);
        //这里可以根据地址的id直接配置, 不用担心是不是当前用户的问题
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null){
            return R.success(addressBook);
        }else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 获取默认的地址
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getIsDefault,1);
        lambdaQueryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());

        AddressBook addressBook = addressBookService.getOne(lambdaQueryWrapper);
        if(addressBook == null){
            return R.error("没有找到该对象");
        }else {
            return R.success(addressBook);
        }
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId((BaseContext.getCurrentId()));
        log.info("addressBook:{}",addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        //根据更新时间排序
        lambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(lambdaQueryWrapper);
        return R.success(list);
    }



}
