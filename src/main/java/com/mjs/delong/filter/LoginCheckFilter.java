package com.mjs.delong.filter;


import com.alibaba.fastjson.JSON;
import com.mjs.delong.common.BaseContext;
import com.mjs.delong.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录校验以及静态资源放行
 */
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //定义不需要处理的请求路径
        String[] uris = new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };



        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("本次请求的路径是: {}",requestURI);


        //2.进行uri检查
        boolean check = check(uris, requestURI);


        //3.如果uri在允许的范围内,则放行
        if (check){
            log.info("本次请求路径通过{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }


        //4.如果不在允许的范围内,查看是否已经登录,如果已登录 放行
        if (request.getSession().getAttribute("employee") != null){
            Long empId = (Long)request.getSession().getAttribute("employee");
            log.info("用户已登录,放行,用户ID为: {}",empId);
            log.info("在当前线程中设置用户id: {}", empId);
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }

        //4-2移动端登录验证
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户已已登录,用户id为:{}",request.getSession().getAttribute("user"));
            Long userId = (Long)request.getSession().getAttribute("user");
            //在线程中设置用户id
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }


        log.info("用户未登录");
        //5.没有登录时的操作
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }

    /**
     * 判断路径是否需要放行
     * @param uris
     * @param requestURI
     * @return
     */

    public boolean check(String[] uris,String requestURI){
        for (String uri : uris) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
