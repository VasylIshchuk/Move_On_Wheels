package org.umcs.repositories.implementation.json;

import com.google.gson.reflect.TypeToken;
import org.umcs.storage.JsonFileStorage;
import org.umcs.models.User;
import org.umcs.repositories.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonUserRepository implements IUserRepository {
    private static JsonUserRepository instance;

    private final List<User> users;
    private final String filePath = "users.json";
    private final JsonFileStorage<User> jsonFileStorage = new JsonFileStorage<>(filePath);

    private JsonUserRepository() {
        this.users = jsonFileStorage.load(new TypeToken<List<User>>() {
        }.getType());
    }

    public static JsonUserRepository getInstance() {
        if (instance == null) {
            instance = new JsonUserRepository();
        }
        return instance;
    }

    @Override
    public void save(User user) {
        users.add(user);
        jsonFileStorage.saveToFile(users);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        for (User user : users) {
            if (user.getLogin().equals(login)) return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public boolean validateUserLogin(String loginFromClient) {
        for (User user : users) {
            if (user.getLogin().equals(loginFromClient)) return false;
        }
        return true;
    }

    @Override
    public List<User> getListClients() {
        List<User>  clients = new ArrayList<>();
        for (User user : users) {
            if (!user.getRole().equals("Client")) continue;
            clients.add(user);
        }
        return clients;
    }
}


