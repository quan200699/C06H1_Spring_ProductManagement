package com.codegym.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {

    @AfterThrowing("execution(public * com.codegym.service.product.ProductService.save(..))")
    public void log() {
        System.out.println("Đây là ghi log lỗi");
    }
}
