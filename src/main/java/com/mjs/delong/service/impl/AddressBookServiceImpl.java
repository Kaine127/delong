package com.mjs.delong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mjs.delong.entity.AddressBook;
import com.mjs.delong.entity.User;
import com.mjs.delong.mapper.AddressBookMapper;
import com.mjs.delong.mapper.UserMapper;
import com.mjs.delong.service.AddressBookService;
import com.mjs.delong.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
