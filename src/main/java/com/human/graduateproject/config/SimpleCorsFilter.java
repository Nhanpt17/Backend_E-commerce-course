package com.human.graduateproject.config;

import com.human.graduateproject.repository.UserRepository;
import com.human.graduateproject.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // dam bao bo loc nay chay dau tien truoc cac bo loc khac
public class SimpleCorsFilter implements Filter {

    @Value("${app.client.url}")
    private String clientAppUrl ="";




    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Map<String,String> map= new HashMap<>();
        String originHeader = request.getHeader("Origin");
        if(clientAppUrl.equals(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", clientAppUrl);
            response.setHeader("Access-Control-Allow-Credentials", "true"); // Cần thiết cho request có credentials
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "*");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
