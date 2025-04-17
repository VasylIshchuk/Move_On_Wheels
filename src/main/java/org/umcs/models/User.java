package org.umcs.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Check;
import org.umcs.storage.JsonSerializable;

import java.util.UUID;

@Entity
@Table(name = "users")
@Check(constraints = "role IN ('ADMIN','CLIENT')")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements JsonSerializable {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
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
