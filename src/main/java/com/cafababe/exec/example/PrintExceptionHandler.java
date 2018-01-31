package com.cafababe.exec.example;

import com.cafababe.exec.handler.ExceptionHandler;

/**
 * 处理异常只需要实现ExceptionHandler
 * @author cafababe
 * @since 1.0
 */
public class PrintExceptionHandler implements ExceptionHandler {

    @Override
    public void executeResult(Exception e) {
        System.out.println(e.getMessage());
    }
}
