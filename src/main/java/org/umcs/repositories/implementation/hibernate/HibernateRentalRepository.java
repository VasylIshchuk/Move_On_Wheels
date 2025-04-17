package org.umcs.repositories.implementation.hibernate;

import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.umcs.models.Rental;
import org.umcs.repositories.IRentalRepository;

import java.util.List;
import java.util.Optional;

@Setter
public class HibernateRentalRepository implements IRentalRepository {
    private Session session;

    @Override
    public void save(Rental rental) {
        session.merge(rental);
    }

    @Override
    public void removeRentalById(String rentalId) {
        Rental rental = session.get(Rental.class, rentalId);
        if (rental != null) {
            session.remove(rental);
        }
    }

    @Override
    public Optional<Rental> findById(String rentalId) {
        return Optional.ofNullable(session.get(Rental.class, rentalId));
    }

    @Override
    public Optional<Rental> findByUserIdAndReturnDateIsNull(String userId) {
        Query<Rental> query = session.createQuery("""
                FROM Rental rental
                WHERE rental.user.id = :userId
                AND rental.returnDate IS NULL
                """, Rental.class);
        query.setParameter("userId", userId);
        return query.uniqueResultOptional();
    }

    @Override
    public List<Rental> getAllRentals() {
        return session.createQuery("FROM Rental", Rental.class).list();
    }
}
