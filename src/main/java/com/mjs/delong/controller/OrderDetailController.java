package com.mjs.delong.controller;

import com.mjs.delong.service.OrderDetailService;
import com.mjs.delong.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/orderDetail")
@RestController
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;


}
