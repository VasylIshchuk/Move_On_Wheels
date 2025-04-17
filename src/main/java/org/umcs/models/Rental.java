package org.umcs.models;

import jakarta.persistence.*;
import lombok.*;
import org.umcs.storage.JsonSerializable;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Entity
@Table(name = "rentals")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental implements JsonSerializable {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    @Column(name = "rent_date", nullable = false)
    private String rentDate;

    @Setter
    @Column(name = "return_date")
    private String returnDate = null;

    public Rental(Vehicle vehicle, User user, String rentDate) {
        this.id = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.user = user;
        this.rentDate = rentDate;
    }

    public String getVehicleId() {
        return vehicle.getId();
    }

    public String getUserId() {
        return user.getId();
    }

    @Override
    public String toJSON() {
        return "\t{\n" +
                "\t\t\"id\": \"" + id + "\",\n" +
                "\t\t\"vehicle\": " + indent(vehicle.toJSON()) + ",\n" +
                "\t\t\"user\": " + indent(user.toJSON()) + ",\n" +
                "\t\t\"rentDate\": \"" + rentDate + "\",\n" +
                "\t\t\"returnDate\": \"" + returnDate + "\"\n\t}";
    }

    private String indent(String json) {
        AtomicBoolean isFirst = new AtomicBoolean(true);

        return json
                .lines()
                .map(line -> isFirst.getAndSet(false) ? line : "\t" + line)
                .collect(Collectors.joining("\n"));
    }
}