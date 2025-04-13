package org.umcs.services;

import org.umcs.models.User;
import org.umcs.repositories.IUserRepository;

import java.util.List;

public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getListClients() {
        return userRepository.getListClients();
    }
}
