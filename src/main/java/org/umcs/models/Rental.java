package org.umcs.models;

import lombok.*;
import org.umcs.storage.JsonSerializable;

import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental implements JsonSerializable {
    private String id;
    private String vehicleId;
    private String userId;
    private String rentDate;
    private String returnDate;

    public Rental(String vehicleId, String userId, String rentDate, String returnDate) {
        this.id = UUID.randomUUID().toString();
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
    }

    @Override
    public String toJSON() {
        return "\t{\n" +
                "\t\t\"id\": \"" + id + "\",\n" +
                "\t\t\"vehicleId\": \"" + vehicleId + "\",\n" +
                "\t\t\"userId\": \"" + userId + "\",\n" +
                "\t\t\"rentDate\": \"" + rentDate + "\",\n" +
                "\t\t\"returnDate\": \"" + returnDate + "\"\n\t}";
    }
}