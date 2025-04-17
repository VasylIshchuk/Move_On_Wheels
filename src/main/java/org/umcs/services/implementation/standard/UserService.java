package org.umcs.services.implementation.standard;

import org.umcs.MenuManager;
import org.umcs.models.User;
import org.umcs.repositories.IUserRepository;
import org.umcs.services.IUserService;

import java.util.List;

public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void showListClients() {
        List<User> listClients = userRepository.getListClients();
        MenuManager.showList(listClients);
    }
}
