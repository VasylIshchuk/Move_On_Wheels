package org.umcs.repositories.implementation.hibernate;

import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.umcs.models.User;
import org.umcs.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;

@Setter
public class HibernateUserRepository implements IUserRepository {
    private Session session;

    @Override
    public void save(User user) {
        session.merge(user);
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(session.get(User.class, userId));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Query<User> query = session.createQuery("""
                FROM User user
                WHERE user.login= :login
                """, User.class);
        query.setParameter("login", login);
        return query.uniqueResultOptional();
    }

    @Override
    public boolean isUserLoginExist(String loginFromClient) {
       Query<User> query = session.createQuery("""
                FROM User user
                WHERE user.login = :login
                """, User.class);
        query.setParameter("login", loginFromClient);

        return !query.getResultList().isEmpty();
    }

    @Override
    public List<User> getListClients() {
        return session.createQuery("FROM User user WHERE user.role = :role", User.class)
                .setParameter("role", "CLIENT")
                .list();
    }
}
