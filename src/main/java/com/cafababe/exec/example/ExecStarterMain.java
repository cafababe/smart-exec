package com.cafababe.exec.example;

import com.cafababe.exec.ExecStarter;

import java.io.IOException;

/**
 * @author cafababe
 * @since 1.0
 */
public class ExecStarterMain {

    public static void main(String[] args) throws IOException {
        new ExecStarter().start("ping 127.0.0.1", new PrintDataHandler());
    }
}
