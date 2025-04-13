package org.umcs.models;

import lombok.*;
import org.umcs.storage.JsonSerializable;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements JsonSerializable {
    private String id;
    private String login;
    private String password;
    private String role;

    public User(String login, String password, String role) {
        this.id = UUID.randomUUID().toString();
        this.login = login;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toJSON() {
        return "\t{\n" +
                "\t\t\"id\": \"" + id + "\",\n" +
                "\t\t\"role\": \"" + role + "\",\n" +
                "\t\t\"login\": \"" + login + "\",\n" +
                "\t\t\"password\": \"" + password + "\"\n\t}";
    }
}
