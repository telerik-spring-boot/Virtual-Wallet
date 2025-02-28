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
    public User getByIdWithRoles(int id) {
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.roles WHERE u.id = :id", User.class);

            query.setParameter("id", id);

            return query.uniqueResult();
        }
    }

    @Override
    public User getByEmail(String email) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User WHERE email = :email", User.class);

            query.setParameter("email", email);

            return query.uniqueResult();
        }
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User WHERE phoneNumber = :phone", User.class);

            query.setParameter("phone", phoneNumber);

            return query.uniqueResult();
        }
    }

    @Override
    public User getByUsername(String username) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User WHERE username = :username", User.class);

            query.setParameter("username", username);

            return query.uniqueResult();
        }
    }

    @Override
    public User getByUsernameWithRoles(String username) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.roles WHERE u.username = :username", User.class);

            query.setParameter("username", username);

            return query.uniqueResult();
        }
    }

    @Override
    public User getUserWithStocks(int id) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.stocks WHERE u.id = :id", User.class);

            query.setParameter("id", id);

            return query.uniqueResult();
        }
    }

    @Override
    public User getUserWithStocksAndWallets(int userId) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.stocks LEFT JOIN FETCH u.wallets WHERE u.id = :userId", User.class);

            query.setParameter("userId", userId);

            return query.uniqueResult();
        }
    }

    @Override
    public List<User> getByAnyUniqueField(String username, String email, String phone) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User where username = :username or email = :email or phoneNumber = :phone", User.class);

            query.setParameter("username", username);
            query.setParameter("email", email);
            query.setParameter("phone", phone);

            return query.list();
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
