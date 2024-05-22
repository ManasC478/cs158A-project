package com.stream.music.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomClients extends Thread {
    private DataInputStream dataInputStream;
    private List<DataOutputStream> dataOutputStreams;
    
    public RoomClients(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        dataOutputStreams = new ArrayList<DataOutputStream>();
    }

    public synchronized void addClientStream(DataOutputStream dos) {
        System.out.println("adding to lenght of "+dataOutputStreams.size());
        dataOutputStreams.add(dos);
    }

    public synchronized void deleteClientStream(DataOutputStream dos) {
        dataOutputStreams.remove(dos);
    }

    private void broadcast(byte[] buffer, int off, int len) {
        synchronized (this) {
            for (DataOutputStream dataOutputStream : dataOutputStreams) {
                try {
                    dataOutputStream.write(buffer, off, len);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcast(byte[] buffer) {
        synchronized (this) {
            System.out.println("Broadcasting to "+dataOutputStreams.size()+" clients");
            for (DataOutputStream dataOutputStream : dataOutputStreams) {
                try {
                    dataOutputStream.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcast(String data) {
        synchronized (this) {
            for (DataOutputStream dataOutputStream : dataOutputStreams) {
                try {
                    dataOutputStream.writeChars(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcast(long data) {
        synchronized (this) {
            for (DataOutputStream dataOutputStream : dataOutputStreams) {
                try {
                    dataOutputStream.writeLong(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void run() {
        readInputStream();
    }

    private void readInputStream() {
        int bytesRead = 0;

        long size;
        try {
            size = dataInputStream.readLong();
            broadcast(size);
            byte[] buffer = new byte[4 * 1024];
            // while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            //     broadcast(buffer, 0, (int)Math.min(buffer.length, size));
            //     System.out.println("Reading "+bytes+ " bytes");
            //     size -= bytes;
            // }
            while (true) {
                bytesRead = dataInputStream.read(buffer);
                if (bytesRead != -1) {
                    broadcast(buffer);
                    System.out.println("Reading "+bytesRead+ " bytes");
                } else {
                    throw new InterruptedException("Room owner closed socket");
                }
            }
        } catch (IOException | InterruptedException e) {
            broadcast("closed");
            dataOutputStreams.clear();
            e.printStackTrace();
        }
    }
}
