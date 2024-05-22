package com.stream.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stream.App;
import com.stream.listener.ThreadEventListener;
import com.stream.model.Response;
import com.stream.model.Room;
import com.stream.util.HttpRequestHandler;
import com.stream.util.StreamFromServer;
import com.stream.util.StreamToServer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ListenerRoomController implements Initializable, ThreadEventListener {
    public static Room room;

    @FXML
    private Label roomName;

    @FXML
    private Label redirectHomeWarning;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private StreamFromServer streamFromServer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        roomName.setText("Room " + room.id + "\nCreated at: " + room.createdAt);
        try {
            socket = new Socket("localhost", 4000);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeLong(room.id);
            streamFromServer = new StreamFromServer(dataInputStream, this);
            streamFromServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectHome() {
        try {
            streamFromServer.stopThread();
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
            App.setRoot("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onThreadEvent(Thread thread, Event event) {
        if (thread == streamFromServer) {
            if (event == Event.THREAD_STOPPED) {
                redirectHome();
            }
        }
    }
}
