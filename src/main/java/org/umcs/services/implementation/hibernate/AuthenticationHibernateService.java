package org.umcs.services.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import org.umcs.MenuManager;
import org.umcs.models.User;
import org.umcs.repositories.implementation.hibernate.HibernateUserRepository;
import org.umcs.services.IAuthenticationService;
import org.umcs.storage.HibernateConfig;

import java.util.Optional;

public class AuthenticationHibernateService implements IAuthenticationService {
    private final HibernateUserRepository userRepository;

    public AuthenticationHibernateService(HibernateUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String login, String password) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            userRepository.setSession(session);

            Optional<User> user = userRepository.findByLogin(login);

            if (user.isEmpty()) {
                MenuManager.showInvalidLoginPrompt();
                return Optional.empty();
            } else if (!validatePassword(password, user.get().getPassword())) {
                MenuManager.showInvalidPasswordPrompt();
                return Optional.empty();
            }

            return user;
        }
    }

    private boolean validatePassword(String password, String passwordFromFile) {
        return BCrypt.checkpw(password, passwordFromFile);
    }

    public boolean register(String login, String password, String role) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            userRepository.setSession(session);

            if (userRepository.isUserLoginExist(login)) {
                MenuManager.showLoginExistsPrompt();
                return false;
            }

            String hashedPassword = hashPassword(password);
            User user = new User(login, hashedPassword, role);

            userRepository.save(user);

            transaction.commit();
        }
        return true;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
