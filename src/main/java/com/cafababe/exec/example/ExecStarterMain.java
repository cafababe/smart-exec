package com.cafababe.exec.example;

import com.cafababe.exec.ExecStarter;

import java.io.IOException;

/**
 * @author cafababe
 * @since 1.0
 */
public class ExecStarterMain {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 300; i++) {
            new Thread(() -> {
                try {
                    new ExecStarter().start("ping 127.0.0.1", new PrintDataHandler(), new PrintExceptionHandler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
