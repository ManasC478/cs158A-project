package com.stream.music.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stream.music.entity.Room;
import com.stream.music.model.ErrorResponse;
import com.stream.music.model.Response;
import com.stream.music.model.ResponseException;
import com.stream.music.model.SuccessResponse;
import com.stream.music.repository.RoomRepository;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomRepository userRepository;
    @PostMapping("/create")
    public ResponseEntity<Response> postRoom() {
        try {
             Room r = userRepository.save( new Room());
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<Room>(r));
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(new ResponseException(e.getMessage(), e.getCause().getMessage())));
        }
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<Response> getRooms() {
        Iterable<Room> rooms = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<Iterable<Room>>(rooms));
    }

    @DeleteMapping("/{roomID}")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomID) {
        
        try {
            userRepository.deleteById(roomID);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<Object>(null));
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(new ResponseException(e.getMessage(), e.getCause().getMessage())));
        }
    }
}
