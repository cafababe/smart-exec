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
public class ReaderThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ReaderThread.class);

    private String command;

    private ByteArrayOutputStream os;

    private boolean shutdown = false;

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
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignore) {
            // nothing
            logger.warn("程序暂停失败");
        }
    }

    /**
     * 处理异常
     */
    private void execException() {
        if (resultHandler.hasResult() && resultHandler.getException() != null) {
            if (dataHandler instanceof ExceptionHandler) {
                ((ExceptionHandler)dataHandler).executeResult(resultHandler.getException());
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

}