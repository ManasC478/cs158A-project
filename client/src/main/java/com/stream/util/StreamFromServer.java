package com.stream.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.input.TeeInputStream;

import com.stream.listener.ThreadEventListener;
import com.stream.listener.ThreadEventListener.Event;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class StreamFromServer extends Thread {
    private ThreadEventListener listener;
    private DataInputStream dataInputStream;
    private boolean stop;
    private boolean playing;

    public StreamFromServer(DataInputStream dataInputStream, ThreadEventListener listener) {
        this.dataInputStream = dataInputStream;
        this.listener = listener;
        stop = false;
        playing = false;
    }

    @Override
    public void run() {
        listener.onThreadEvent(this, Event.THREAD_STARTED);
        streamFromServer();
        listener.onThreadEvent(this, Event.THREAD_STOPPED);
    }

    public synchronized void stopThread() {
        stop = true;
    }
    
    private void streamFromServer() {
        try {
            synchronized (this) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                TeeInputStream teeInputStream = new TeeInputStream(dataInputStream, baos, true);
                Player player = new Player(teeInputStream);
                player.play();
                while (!stop) {
                    // if (!playing) {
                    //     playing = true;
                    // }
                    String data = dataInputStream.readUTF();
                    System.out.println(data);
                    if (data.indexOf("closed") > -1) {
                        stop = true;
                    }
                    else if (data.indexOf("start") > -1) {
                        listener.onThreadEvent(this, Event.MUSIC_STREAMING_STARTED);
                    }
                    else if (data.indexOf("stop") > -1) {
                        System.out.println("stopped music streaming");
                        listener.onThreadEvent(this, Event.MUSIC_STREAMING_STOPPED);
                    }
                }
                
                player.close();
                teeInputStream.close();
            }
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }
}
