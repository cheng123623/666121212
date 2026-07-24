package com.sky.interceptor;

import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = null;
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/admin")) {
            token = request.getHeader(jwtProperties.getAdminTokenName());
            if (token != null) {
                Claims claims = jwtUtil.parseJWT(token, jwtProperties.getAdminSecretKey());
                Long userId = Long.valueOf(claims.get("userId").toString());
                BaseContext.setCurrentId(userId);
            }
        } else if (requestURI.startsWith("/user")) {
            token = request.getHeader(jwtProperties.getUserTokenName());
            if (token != null) {
                Claims claims = jwtUtil.parseJWT(token, jwtProperties.getUserSecretKey());
                Long userId = Long.valueOf(claims.get("userId").toString());
                BaseContext.setCurrentId(userId);
            }
        }

        return true;
    }
}
