package com.cafababe.exec.handler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author cafababe
 * @since 1.0
 */
public class DataHandlerChain implements DataHandler, ExceptionHandler {

    private List<DataHandler> handlers = new LinkedList<>();

    private List<DataHandler> exceptionHandlers = new LinkedList<>();

    public DataHandlerChain() {}

    public DataHandlerChain(DataHandler... resultHandlers) {
        addAllHandler(resultHandlers);
    }

    /**
     * 正常处理返回结果
     * @param line 一行数据
     */
    @Override
    public void executeResult(String line) {
        for (DataHandler handler : handlers) {
            handler.executeResult(line);
        }
    }

    /**
     * 处理异常
     * @param e 抛出异常
     */
    @Override
    public void executeResult(Exception e) {
        for (DataHandler exceptionHandler : exceptionHandlers) {
            ((ExceptionHandler)exceptionHandler).executeResult(e);
        }
    }

    public DataHandlerChain addHandler(DataHandler resultHandler) {
        if (resultHandler instanceof ExceptionHandler) {
            exceptionHandlers.add(resultHandler);
        } else {
            handlers.add(resultHandler);
        }
        return this;
    }

    public DataHandlerChain addAllHandler(DataHandler... resultHandlers) {
        for (DataHandler resultHandler : resultHandlers) {
            addHandler(resultHandler);
        }
        return this;
    }

    public static DataHandler getDefaultDataHandler() {
        return new VoidDataHandler();
    }


    private static class VoidDataHandler implements DataHandler {

        @Override
        public void executeResult(String line) {
            // Do nothing
        }
    }
}
