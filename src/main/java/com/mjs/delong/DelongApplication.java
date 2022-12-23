package com.mjs.delong;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan//开始对Filter Listener 的支持
@EnableTransactionManagement //开启对事物管理的支持
public class DelongApplication {
    public static void main(String[] args) {
        SpringApplication.run(DelongApplication.class,args);
        log.info("项目启动成功");
    }
}
