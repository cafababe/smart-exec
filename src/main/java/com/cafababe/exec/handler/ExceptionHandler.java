package com.cafababe.exec.handler;

/**
 * 处理异常类
 * @author cafababe
 * @since 1.0
 */
public interface ExceptionHandler extends DataHandler {

    /**
     * 处理异常
     * @param e 抛出异常
     */
    void executeResult(Exception e);

    /**
     * 默认情况下ExceptionHandler不处理正常数据
     * @param line 一行数据
     */
    @Override
    default void executeResult(String line) {
    }
}
