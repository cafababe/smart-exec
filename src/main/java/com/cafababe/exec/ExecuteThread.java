package com.cafababe.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;

/**
 * @author cafababe
 * @since 1.0
 */
public class ExecuteThread implements Runnable {

    private DefaultExecutor executor;

    private CommandLine command;

    private DefaultExecuteResultHandler handler;

    public ExecuteThread(DefaultExecutor executor, CommandLine command, DefaultExecuteResultHandler handler) {
        this.executor = executor;
        this.command = command;
        this.handler = handler;
    }

    @Override
    public void run() {
        int exitValue = Executor.INVALID_EXITVALUE;
        try {
            exitValue = executor.execute(command);
            handler.onProcessComplete(exitValue);
        } catch (final ExecuteException e) {
            handler.onProcessFailed(e);
        } catch (final Exception e) {
            handler.onProcessFailed(new ExecuteException("Execution failed", exitValue, e));
        }
    }
}
