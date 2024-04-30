package com.stream.music.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stream.music.entity.Room;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {}
