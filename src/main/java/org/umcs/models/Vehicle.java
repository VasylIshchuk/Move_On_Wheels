package org.umcs.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import org.umcs.storage.JsonSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle implements JsonSerializable {
    private String id;
    private String category;
    private String registrationNumber;
    private String brand;
    private String model;
    private int year;
    private double price;
    private boolean rented;
    @Builder.Default
    private Map<String, Object> attributes = Map.of();

    public Vehicle(String category, String registrationNumber, String brand, String model, int year, double price) {
        this.id = UUID.randomUUID().toString();
        this.category = category;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = false;
        this.attributes = new HashMap<>();
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public String toJSON() {
        return "\t{\n" +
                "\t\t\"id\": \"" + id + "\",\n" +
                "\t\t\"category\": \"" + category + "\",\n" +
                "\t\t\"registrationNumber\": \"" + registrationNumber + "\",\n" +
                "\t\t\"brand\": \"" + brand + "\",\n" +
                "\t\t\"model\": \"" + model + "\",\n" +
                "\t\t\"year\": " + year + ",\n" +
                "\t\t\"price\": " + price + ",\n" +
                "\t\t\"rented\": " + rented + ",\n" +
                formatAttributesAsJson() + "\n\t}";
    }

    private String formatAttributesAsJson() {
        StringBuilder json = new StringBuilder("\t\t\"attributes\": {\n");
        int counter = 0;

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {

            json.append("\t\t\t\"")
                    .append(entry.getKey())
                    .append("\": ")
                    .append("\"")
                    .append(entry.getValue())
                    .append("\"");

            if (counter < attributes.size() - 1) json.append(",\n");

            ++counter;
        }

        json.append("\n\t\t}");

        return json.toString();
    }
}
