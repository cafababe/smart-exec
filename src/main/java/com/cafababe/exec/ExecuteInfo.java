package com.cafababe.exec;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;

/**
 * @author cafababe
 * @since 1.0
 */
public class ExecuteInfo {

    private ReaderThread thread;

    private ExecuteStreamHandler handler;

    private ExecuteWatchdog watchdog;

    private DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

    public ExecuteInfo(ReaderThread thread, ExecuteStreamHandler handler, ExecuteWatchdog watchdog) {
        this.thread = thread;
        this.handler = handler;
        this.watchdog = watchdog;
    }

    public ReaderThread getThread() {
        return thread;
    }

    public void setThread(ReaderThread thread) {
        this.thread = thread;
    }

    public ExecuteStreamHandler getHandler() {
        return handler;
    }

    public void setHandler(ExecuteStreamHandler handler) {
        this.handler = handler;
    }

    public ExecuteWatchdog getWatchdog() {
        return watchdog;
    }

    public void setWatchdog(ExecuteWatchdog watchdog) {
        this.watchdog = watchdog;
    }

    public DefaultExecuteResultHandler getResultHandler() {
        return resultHandler;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (!resultHandler.hasResult() && !watchdog.killedProcess()) {
            // 关闭命令行线程
            watchdog.destroyProcess();
            // 关闭读取线程
            thread.setShutdown(true);
        }
    }
}
