package com.telerik.virtualwallet.repositories.user;

import com.telerik.virtualwallet.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{


    private final SessionFactory sessionFactory;


    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);

            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {

            return session.get(User.class, id);

        }
    }

    @Override
    public void create(User user) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.merge(user);

            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            User user = session.get(User.class, id);

            session.remove(user);

            session.getTransaction().commit();
        }
    }
}
