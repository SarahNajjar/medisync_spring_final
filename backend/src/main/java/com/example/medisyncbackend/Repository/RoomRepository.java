package com.example.medisyncbackend.Repository;

import com.example.medisyncbackend.Models.Room;
import com.example.medisyncbackend.Models.Room.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r WHERE " +
            "(:id IS NULL OR r.roomId = :id) AND " +
            "(:name IS NULL OR LOWER(r.roomName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:type IS NULL OR LOWER(r.roomType) LIKE LOWER(CONCAT('%', :type, '%'))) AND " +
            "(:status IS NULL OR r.status = :status)")
    List<Room> findFilteredRooms(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("type") String type,
            @Param("status") RoomStatus status
    );

    @Query("SELECT r.status FROM Room r WHERE r.roomId = :id")
    RoomStatus getRoomStatus(@Param("id") Integer id);
}
