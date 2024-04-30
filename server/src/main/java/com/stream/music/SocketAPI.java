package com.stream.music;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class SocketAPI implements Runnable {
    // HashMap<
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
//    private HashMap<int, >
    private Socket clientSocket;
    private final ServerSocket serverSocket;
    public SocketAPI(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            receiveFile();
        } catch (IOException e) {
            try {
                dataInputStream.close();
                dataOutputStream.close();
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


    }

    private void receiveFile() throws IOException{
        int bytes = 0;

        long size = dataInputStream.readLong();     // read file size
        dataOutputStream.writeLong(size);
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            dataOutputStream.write(buffer, 0, (int)Math.min(buffer.length, size));
            System.out.println("Reading "+bytes+ " bytes");
            size -= bytes;      // read upto file size
        }
    }

}
