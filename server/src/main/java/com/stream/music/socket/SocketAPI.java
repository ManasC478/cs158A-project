package com.stream.music.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import com.stream.music.model.RoomClients;

public class SocketAPI implements Runnable {
    HashMap<Long, RoomClients> roomIdToRoomClients;
    // private DataOutputStream dataOutputStream;
    // private DataInputStream dataInputStream;
    private final ServerSocket serverSocket;
    public SocketAPI(int port) {
        try {
            serverSocket = new ServerSocket(port);
            roomIdToRoomClients = new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                long roomId = dataInputStream.readLong();
                System.out.println(roomId);
                System.out.println("room exists "+roomIdToRoomClients.containsKey(roomId));
                if (!roomIdToRoomClients.containsKey(roomId)) {
                    roomIdToRoomClients.put(roomId, new RoomClients(dataInputStream));
                    roomIdToRoomClients.get(roomId).start();
                }
                System.out.println("adding client...");
                roomIdToRoomClients.get(roomId).addClientStream(dataOutputStream);
                System.out.println("added to room "+roomId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // private void receiveFile() throws IOException {
    //     System.out.println("receivingFile");
    //     int bytes = 0;

    //     long size = dataInputStream.readLong();     // read file size
    //     dataOutputStream.writeLong(size);
    //     byte[] buffer = new byte[4*1024];
    //     while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
    //         dataOutputStream.write(buffer, 0, (int)Math.min(buffer.length, size));
    //         System.out.println("Reading "+bytes+ " bytes");
    //         size -= bytes;      // read upto file size
    //     }
    // }

}
