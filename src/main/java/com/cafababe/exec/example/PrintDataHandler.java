package com.cafababe.exec.example;

import com.cafababe.exec.handler.DataHandler;

/**
 * 正常数据处理只需要实现DataHandler
 * @author cafababe
 * @since 1.0
 */
public class PrintDataHandler implements DataHandler {

    @Override
    public void executeResult(String line) {
        System.out.println(line);
    }
}
