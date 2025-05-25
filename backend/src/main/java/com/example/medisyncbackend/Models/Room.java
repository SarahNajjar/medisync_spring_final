package com.example.medisyncbackend.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    private String roomName;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private String roomType;

    public enum RoomStatus {
        Available, Occupied, Under_Maintenance
    }

    public Room() {}

    public Room(Integer roomId, String roomName, String roomType, RoomStatus status) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomType = roomType;
        this.status = status;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", status=" + status +
                '}';
    }
}
