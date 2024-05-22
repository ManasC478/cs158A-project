package com.stream.controller;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class OwnerRoomController implements Initializable, ThreadEventListener{
    public static Room room;
    @FXML
    private Label fileWarning;

    @FXML
    private Label selectFile;

    @FXML
    private Label roomName;

    @FXML
    private Label redirectHomeWarning;

    @FXML
    private Button playBtn;
    
    @FXML
    private Button uploadFileBtn;
    
    // @FXML
    // private Button pauseBtn;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    // private File streamFile;
    private Socket socket;
    private StreamToServer streamToServer;
    private StreamFromServer streamFromServer;
    private boolean startedStream;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        roomName.setText("Room " + room.id + "\nCreated at: " + room.createdAt);
        try {
            socket = new Socket("localhost", 4000);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeLong(room.id);
            streamToServer = new StreamToServer(dataOutputStream, this);
            streamFromServer = new StreamFromServer(dataInputStream, this);
            streamFromServer.start();
            startedStream = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void play() {
        if (streamToServer.getFile() == null) {
            return;
        }
        // if (!startedStream) {
            streamToServer.start();
            startedStream = false;
        // } else {
        //     streamToServer.play();
        // }

        playBtn.setDisable(true);
        uploadFileBtn.setDisable(true);
        // pauseBtn.setDisable(false);
        // CountDownLatch latch = new CountDownLatch(2);
        // Thread senderThread = new Thread(() -> {
        //     sendFile();
        //     latch.countDown();
        // });
        // Thread readerThread = new Thread(() -> {
        //     // readStream();
        //     try {
        //         dataInputStream.readLong();
        //         Player player = new Player(dataInputStream);
        //         player.play();
        //     } catch (IOException | JavaLayerException e) {
        //         e.printStackTrace();
        //     }
        //     latch.countDown();
        // });
        
        
        // senderThread.start();
        // readerThread.start();

        // try {
        //     latch.await();
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
    }

    public void pause() {
        // streamToServer.pause();

        // playBtn.setDisable(false);
        // pauseBtn.setDisable(true);
    }

    public void uploadFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.mainStage);
        if (file == null) {
            return;
        }
        String[] splitFilePath = file.getPath().split("\\.");
        selectFile.setText(file.getPath());
        if (!splitFilePath[splitFilePath.length - 1].equals("mp3")) {
            fileWarning.setVisible(true);
            streamToServer.setFile(null);
        } else {
            fileWarning.setVisible(false);
            streamToServer.setFile(file);
            playBtn.setDisable(false);
        }
    }

    public void redirectHome() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpRequestHandler requestHandler = new HttpRequestHandler("http://localhost:8080/room/" + room.id);
            requestHandler.setMethod(HttpRequestHandler.Method.DELETE);
            Response<Room> body = mapper.readValue(requestHandler.send(),
            new TypeReference<Response<Room>>() {
            });
            if (body.data == null) {
                requestHandler.printError(body.error);
                redirectHomeWarning.setVisible(true);
            } else {
                OwnerRoomController.room = null;
                App.setRoot("home");
            }
            streamToServer.stopThread();
            streamFromServer.stopThread();
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onThreadEvent(Thread thread, Event event) {
        if (thread == streamFromServer) {
            if (event == Event.MUSIC_STREAMING_STOPPED) {
                System.out.println("done streaming music");
                streamToServer = new StreamToServer(dataOutputStream, this);
                selectFile.setText("No file selected");
                uploadFileBtn.setDisable(false);
            }
        }
    }
    // private void readStream() {
    //     try {
    //         System.out.println("in read stream");
    //         int bytes = 0;

    //         long size = dataInputStream.readLong();     // read file size
    //         System.out.println("Received file is "+size+" bytes large");
    //         byte[] buffer = new byte[4*1024];
    //         while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
    //             System.out.println("Read "+bytes+ " bytes");
    //             size -= bytes;      // read upto file size
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }

    // }

    // private void sendFile() {
    //     try(FileInputStream fileInputStream = new FileInputStream(streamFile)){
    //         int bytes = 0;
    //         dataOutputStream.writeLong(streamFile.length());
    //         // break file into chunks
    //         byte[] buffer = new byte[4*1024];
    //         while ((bytes=fileInputStream.read(buffer))!=-1){
    //             System.out.println("Writing "+bytes+" bytes");
    //             dataOutputStream.write(buffer,0,bytes);
    //             dataOutputStream.flush();
    //         }
    //     } catch (IOException e){
    //         e.printStackTrace();
    //     }
    // }
}
