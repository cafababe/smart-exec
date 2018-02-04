package com.cafababe.exec;

import com.jcraft.jsch.*;

public class RemoteExecStarter {

    private String username;

    private String password;

    private String host;

    private int port;


    public RemoteExecStarter(String password) {
        this("root", password, "localhost", 22);
    }

    public RemoteExecStarter(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public void start(String command) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.connect();

        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);


    }
}
