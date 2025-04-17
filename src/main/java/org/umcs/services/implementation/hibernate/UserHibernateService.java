package org.umcs.services.implementation.hibernate;

import org.hibernate.Session;
import org.umcs.MenuManager;
import org.umcs.models.User;
import org.umcs.repositories.implementation.hibernate.HibernateUserRepository;
import org.umcs.services.IUserService;
import org.umcs.storage.HibernateConfig;

import java.util.List;

public class UserHibernateService implements IUserService {
    private final HibernateUserRepository userRepository;

    public UserHibernateService(HibernateUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void showListClients() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepository.setSession(session);
            List<User> listClients = userRepository.getListClients();
            MenuManager.showList(listClients);
        }
    }
}
