package org.umcs.repositories;

import org.umcs.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    void save(User user);

    Optional<User> findByLogin(String login);

    boolean validateUserLogin(String loginFromClient);

    List<User> getListClients();
}
