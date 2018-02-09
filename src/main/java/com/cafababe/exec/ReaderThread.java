package com.cafababe.exec;

import com.cafababe.exec.handler.DataHandler;
import com.cafababe.exec.handler.ExceptionHandler;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @author cafababe
 * @since 1.0
 */
public class ReaderThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ReaderThread.class);

    private String command;

    private ByteArrayOutputStream os;

    private volatile boolean shutdown = false;

    private DefaultExecuteResultHandler resultHandler;

    private DataHandler dataHandler;

    public ReaderThread(ByteArrayOutputStream os) {
        this.os = os;
    }

    public ReaderThread(ByteArrayOutputStream os, String command) {
        this.os = os;
        this.command = command;
    }

    @Override
    public void run() {
        while (!shutdown && !resultHandler.hasResult()) {
            getResult();
        }
        // 最后调用，防止resultHandler.hasResult返回结果为true，但是output中还有数据，防止数据不一致
        getResult();
        execException();

    }

    private void getResult() {
        if (os != null && os.size() > 0) {
            try {
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                os.reset();
                InputStreamReader isr = new InputStreamReader(is, "gbk");
                BufferedReader br = new BufferedReader(isr);
                String line;
                while (null != (line = br.readLine())) {
                    if (line.trim().length() != 0) {
                        // 处理数据
                        dataHandler.executeResult(line);
                    }
                }
            } catch (IOException e) {
                logger.error("正式执行命令:{}有IO异常", command);
            }
        }
        wait(100);
    }

    /**
     * 异常处理
     */
    protected void execException() {
        execException(null);
    }

    /**
     *  step 1:判断是否有异常
     *  step 2:是否是ExceptionHandler,只有是ExceptionHandler才处理异常
     *  step 3:交给ExceptionHandler处理异常
     *
     * 异常处理
     * @param e 将要处理的异常
     */
    protected void execException(Exception e) {

        // 判断是否异常
        if (resultHandler.hasResult() && resultHandler.getException() != null || e != null) {
            if (dataHandler instanceof ExceptionHandler) {
                ((ExceptionHandler)dataHandler).executeResult(e != null ? e : resultHandler.getException());
            }
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ByteArrayOutputStream getOs() {
        return os;
    }

    public void setOs(ByteArrayOutputStream os) {
        this.os = os;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public DefaultExecuteResultHandler getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(DefaultExecuteResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    private void wait(int time) {
        // 等待数据
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }
}
