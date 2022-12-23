package com.mjs.delong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mjs.delong.entity.Category;
import com.mjs.delong.entity.Setmeal;
import com.mjs.delong.entity.SetmealDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void deleteWithDish(List<Long> ids);
}
