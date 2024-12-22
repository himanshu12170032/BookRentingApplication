package com.example.bookrent.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String logMessage = String.format(
                "[%s] Request: %s %s",
                LocalDateTime.now(),
                request.getMethod(),
                request.getRequestURI()
        );
        System.out.println(logMessage);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        System.out.printf("[%s] PostHandle - Status: %d%n",
                LocalDateTime.now(),
                response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.printf("[%s] AfterCompletion - Completed request for: %s%n",
                LocalDateTime.now(),
                request.getRequestURI());

        if (ex != null) {
            System.err.printf("[%s] Exception: %s%n", LocalDateTime.now(), ex.getMessage());
        }
    }
}
