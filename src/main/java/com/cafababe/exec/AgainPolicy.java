package com.cafababe.exec;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 当线程池无法处理命令，捕捉异常，只处理ReaderThread线程，再次执行{@code execute()}。
 * 如果Executor已经关闭，将执行异常处理。
 *
 * @author cafababe
 * @since 1.0
 */
class AgainPolicy implements RejectedExecutionHandler {

    /**
     * reject线程处理
     * @param r 线程
     * @param executor 线程池
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof ReaderThread) {
            // 判断是否关闭
            if (executor.isShutdown()) {
                ((ReaderThread)r).execException(new RejectedExecutionException("ExecStarter已经关闭，无法执行命令"));
            } else {
                // 等待队列可用，如果runnable一直无法加入队列，将导致StackOverFlowError错误 例如：执行ping 127.0.0.1 -t
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                executor.execute(r);
            }
        }
    }
}
