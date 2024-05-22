package com.stream.controller;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stream.App;
import com.stream.model.Response;
import com.stream.model.Room;
import com.stream.util.HttpRequestHandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeController implements Initializable {
    @FXML
    private VBox roomButtonGroup;

    @FXML
    private Button createRoom;

    @FXML
    private Label createRoomWarning;

    @FXML
    private Label redirectHome;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpRequestHandler requestHandler = new HttpRequestHandler("http://localhost:8080/room");
            requestHandler.setMethod(HttpRequestHandler.Method.GET);
            Response<List<Room>> body = mapper.readValue(requestHandler.send(),
                    new TypeReference<Response<List<Room>>>() {
                    });
            if (body.data == null) {
                requestHandler.printError(body.error);
            } else {
                for (Room r : body.data) {
                    Button button = new Button("Room " + r.id);
                    button.setOnAction(event -> {
                        joinRoom(r);
                    });
                    roomButtonGroup.getChildren().add(button);
                }
            }

        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinRoom(Room r) {
        ListenerRoomController.room = r;
        try {
            App.setRoot("listenerroom");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void createRoom() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpRequestHandler requestHandler = new HttpRequestHandler("http://localhost:8080/room/create");
            requestHandler.setMethod(HttpRequestHandler.Method.POST, BodyPublishers.ofString(""));
            Response<Room> body = mapper.readValue(requestHandler.send(),
                    new TypeReference<Response<Room>>() {
                    });
            if (body.data == null) {
                requestHandler.printError(body.error);
                createRoomWarning.setVisible(true);
            } else {
                OwnerRoomController.room = body.data;
                App.setRoot("ownerroom");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
