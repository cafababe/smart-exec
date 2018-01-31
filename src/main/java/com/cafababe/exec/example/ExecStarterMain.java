package com.cafababe.exec.example;

import com.cafababe.exec.ExecStarter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author cafababe
 * @since 1.0
 */
public class ExecStarterMain {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 800; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ExecStarter.start("ping 127.0.0.1", new PrintDataHandler(), new PrintExceptionHandler());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
