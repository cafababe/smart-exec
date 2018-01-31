package com.cafababe.exec.handler;

/**
 *
 * 对数据进行处理只需要实现{@link DataHandler}接口
 *
 * @author cafababe
 * @since 1.0
 *
 */
public interface DataHandler {

    /**
     * 处理结果
     * @param line 一行数据
     */
    void executeResult(String line);
}
