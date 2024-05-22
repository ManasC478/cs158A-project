package com.stream.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.stream.listener.ThreadEventListener;
import com.stream.listener.ThreadEventListener.Event;

public class StreamToServer extends Thread {
    private File streamFile;
    private ThreadEventListener listener;
    private DataOutputStream dataOutputStream;
    private boolean play;
    private boolean stop;

    public StreamToServer(DataOutputStream dataOutputStream, ThreadEventListener listener) {
        this.listener = listener;
        this.dataOutputStream = dataOutputStream;
        stop = false;
        play = false;
    }

    @Override
    public void run() {
        listener.onThreadEvent(this, Event.THREAD_STARTED);
        play = true;
        streamToServer();
        listener.onThreadEvent(this, Event.THREAD_STOPPED);
    }

    // public void play() {
    //     play = true;
    //     streamToServer();
    // }

    // public void pause() {
    //     play = false;
    // }

    public synchronized void stopThread() {
        stop = true;
    }

    public void setFile(File file) {
        streamFile = file;
    }

    public File getFile() {
        return streamFile;
    }

    private void streamToServer() {
        try (FileInputStream fileInputStream = new FileInputStream(streamFile)) {
            System.out.println("streaming to server...");
            int bytes = 0;
            dataOutputStream.writeLong(streamFile.length());
            // break file into chunks
            byte[] buffer = new byte[4 * 1024];
            synchronized (this) {
                dataOutputStream.writeChars("start");
                while (!stop && (bytes = fileInputStream.read(buffer)) != -1) {
                    // synchronized (this) {
                    //     while (!play) {
                    //         System.out.println("waiting...");
                    //         this.wait();
                    //     }
                    // }
                    System.out.println("Writing " + bytes + " bytes");
                    dataOutputStream.write(buffer, 0, bytes);
                    dataOutputStream.flush();
                    // if (play) {
                    // } else {
                    //     break;
                    // }
                }
                System.out.println("stopped writing streams");
                dataOutputStream.writeChars("stop");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
