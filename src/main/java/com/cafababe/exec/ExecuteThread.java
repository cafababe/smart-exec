package com.cafababe.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;

/**
 * 执行命令每个线程都拥有一个{@link DefaultExecutor}对象。
 *
 * @author cafababe
 * @since 1.0
 */
public class ExecuteThread implements Runnable {

    private CommandLine command;

    private DefaultExecuteResultHandler handler;

    private ExecuteInfo executeInfo;

    /** java 8 语法 */
    private static ThreadLocal<DefaultExecutor> defaultExecutor = ThreadLocal.withInitial(DefaultExecutor::new);

    public ExecuteThread(CommandLine command, DefaultExecuteResultHandler handler, ExecuteInfo executeInfo) {
        this.command = command;
        this.handler = handler;
        this.executeInfo = executeInfo;
    }

    @Override
    public void run() {

        DefaultExecutor executor = defaultExecutor.get();
        executor.setWatchdog(executeInfo.getWatchdog());

        int exitValue = Executor.INVALID_EXITVALUE;
        try {
            executor.setStreamHandler(executeInfo.getHandler());
            exitValue = executor.execute(command);
            handler.onProcessComplete(exitValue);
        } catch (final ExecuteException e) {
            handler.onProcessFailed(e);
        } catch (final Exception e) {
            handler.onProcessFailed(new ExecuteException("Execution failed", exitValue, e));
        }
    }
}
