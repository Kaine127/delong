package com.mjs.delong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mjs.delong.common.R;
import com.mjs.delong.entity.Employee;
import com.mjs.delong.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        //1. 将页面提交的password进行md5处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//十六进制

        //2. 查询Employee表中是否对应username的用户数据
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<Employee>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);

        //3. 如果没有查询到则返回登录失败
        if (emp == null){
            return R.error("登录失败");
        }

        //4. 如果查询到了就比较两个password,如果不正确,则返回登录失败
        if (!password.equals(emp.getPassword())){
            return R.error("登录失败");
        }

        //5.  如果密码比对成功,则检查用户状态,如果禁用,返回登录失败
        if (emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //6. 如果状态没有问题,将登录的用户的id存入到session中,用于后续操作
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    /**
     * 登出逻辑
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employ");
        return R.success("退出成功");
    }

    /**
     * 员工添加
     */

    @PostMapping
    public R<Employee> saveEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info("增加新员工:{}",employee);
        //设置默认密码为123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置操作的时间和人员
        //创建与更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long empId = (Long)request.getSession().getAttribute("employee");
//
//        //创建与更新人员
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);


        return R.success(employee);
    }

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {}, pageSize = {}, name = {}",page,pageSize,name);
        //获取分页构造器
        Page pageInfo = new Page(page, pageSize);

        //建立查询条件
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //是否添加过滤查询
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //添加根据更新时间倒叙排序
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        //查询
        employeeService.page(pageInfo, lambdaQueryWrapper);

        return R.success(pageInfo);

    }

    /**
     * 更新员工状态
     * @param request
     * @param employee
     * @return
     */
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("员工信息更新: {}",employee.toString());

        //获取当前用户名用于更新
//        Long empId = (Long)request.getSession().getAttribute("employee");

        //设置Employee
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
//        //更新
        employeeService.updateById(employee);

        return R.success("员工信息更新成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable Long id){
        System.out.println(id);
        log.info("根据员工id查询员工信息,id= {}",id);
        //根据id查询员工信息
        Employee emp = employeeService.getById(id);
        if (emp != null){
            log.info("查询到用户: {}",emp);
            return R.success(emp);
        }
        //没有查询到的时候
        return R.error("没有查询到对应员工的信息");
    }


    /**
     * 根据id修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info("员工信息更新: {}",employee.toString());
        //加入更新时状态值(时间和更新人员)
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateUser(empId);
        //更新员工信息
        employeeService.updateById(employee);

        return R.success("员工信息更新完成");

    }

}
