package com.mjs.delong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mjs.delong.entity.AddressBook;
import com.mjs.delong.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
