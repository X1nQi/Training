package com.fixer.httpServer;

import java.io.IOException;
import java.net.Socket;

public class newThread extends Thread{
    private Socket socket;

    public newThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            ParseRequest ParseRequest = new ParseRequest(this.socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
