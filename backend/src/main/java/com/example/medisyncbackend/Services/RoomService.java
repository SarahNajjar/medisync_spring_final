package com.example.medisyncbackend.Services;

import com.example.medisyncbackend.Models.Room;
import com.example.medisyncbackend.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Create or Update room
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by ID
    public Room getRoomById(Integer roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    // Delete room by ID
    public boolean deleteRoom(Integer roomId) {
        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
            return true;
        }
        return false;
    }

    // Filter rooms by status, name, and type
    public List<Room> findFilteredRooms(Integer id, String name, String type, Room.RoomStatus status) {
        return roomRepository.findFilteredRooms(id, name, type, status);
    }

    // Get room status (business logic)
    public Room.RoomStatus getRoomStatus(Integer roomId) {
        return roomRepository.getRoomStatus(roomId);
    }
}
