package com.cafababe.exec;

import com.cafababe.exec.handler.DataHandler;
import com.cafababe.exec.handler.DataHandlerChain;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * <dt>data handler</dt>
 *
 * <dd>可以不设置DataHandler，默认情况下使用{@link DataHandlerChain.VoidDataHandler}，如果只设置一个DataHandler，
 * 那么不会创建{@link DataHandlerChain}对象。如果设置超过一个DataHandler,会将传入的DataHandler存储在{@link DataHandlerChain}。
 * 存在一种特殊的只处理异常的DataHandler{@link com.cafababe.exec.handler.ExceptionHandler}，只需要实现ExceptonHandler并且
 * 将其注册，ExecStarter将会处理异常。如果未注册ExceptionHandler，异常将会抛弃，不进行任何处理。<dd/>
 *
 * <dt>timeout</dt>
 * <dd>如果命令执行时间超过timeout，连接将中断</dd>
 *
 * @author cafababe
 * @since 1.0
 */
public class ExecStarter {


    public static void main(String[] args) throws Exception {
        new ExecStarter().start("ping 127.0.0.1");
    }

    public ExecuteInfo start(String command, DataHandler... resultHandlers) throws IOException {

        CommandLine commandLine = CommandLine.parse(command);
        ExecuteInfo info = getDefaultHandler();

        info.getThread().setCommand(command);

        runtime(info, commandLine, resultHandlers);

        return info;
    }

    public ExecuteInfo start(String command, long timeout, DataHandler... resultHandlers) throws IOException{

        CommandLine commandLine = CommandLine.parse(command);
        ExecuteInfo info = getDefaultHandler(timeout);

        info.getThread().setCommand(command);

        runtime(info, commandLine, resultHandlers);

        return info;
    }

    /**
     * 执行命令
     * @param executeInfo exec运行需要的信息
     * @param commandLine 执行的命令行
     * @param dataHandlers 数据处理
     * @throws IOException
     * @see org.apache.commons.exec.Executor#execute(CommandLine, java.util.Map, ExecuteResultHandler)
     */
    private static void runtime(ExecuteInfo executeInfo, CommandLine commandLine, DataHandler... dataHandlers) throws IOException {

        DefaultExecutor exec = new DefaultExecutor();

        executeInfo.getThread().setResultHandler(executeInfo.getResultHandler());
        // 多个handler使用chain，没有dataHandler的使用默认的dataHandler
        DataHandler dataHandler = dataHandlers.length > 1 ? new DataHandlerChain(dataHandlers) :
                dataHandlers.length == 0 ? DataHandlerChain.getDefaultDataHandler() : dataHandlers[0];

        executeInfo.getThread().setDataHandler(dataHandler);

        exec.setStreamHandler(executeInfo.getHandler());
        exec.setWatchdog(executeInfo.getWatchdog());
        exec.execute(commandLine, executeInfo.getResultHandler());

        executeInfo.getThread().start();
    }

    /**
     * 默认的单次连接超时时间为30分钟
     * @return
     */
    public static ExecuteInfo getDefaultHandler() {
        return getDefaultHandler(1800000);
    }

    /**
     * 可设置timeout
     * @param timeout 超时时间
     * @return
     */
    public static ExecuteInfo getDefaultHandler(long timeout) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        // 创建读取线程
        ReaderThread readerThread = new ReaderThread(outputStream);
        // 超过30分钟，连接自动断开
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);

        return new ExecuteInfo(readerThread, streamHandler, watchdog);
    }

}
