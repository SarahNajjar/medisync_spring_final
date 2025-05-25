package com.example.medisyncbackend.Controllers;

import com.example.medisyncbackend.Models.Room;
import com.example.medisyncbackend.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Integer id) {
        return roomService.getRoomById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room saveRoom(@RequestBody Room room) {
        return roomService.saveRoom(room);
    }

    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable Integer id, @RequestBody Room room) {
        room.setRoomId(id);
        return roomService.saveRoom(room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
    }

    @GetMapping("/search")
    public List<Room> findFilteredRooms(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Room.RoomStatus status) {
        return roomService.findFilteredRooms(id, name, type, status);
    }

    @GetMapping("/{id}/status")
    public Room.RoomStatus getRoomStatus(@PathVariable Integer id) {
        return roomService.getRoomStatus(id);
    }
}
