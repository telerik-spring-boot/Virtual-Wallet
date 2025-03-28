package com.telerik.virtualwallet.repositories.user;

import com.telerik.virtualwallet.models.Referral;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.models.filters.FilterUserOptions;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository{


    private final SessionFactory sessionFactory;


    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Page<User> getAll(FilterUserOptions options, Pageable pageable) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder("FROM User");
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            options.getUsername().ifPresent(value ->{
                filters.add("username LIKE :username");
                params.put("username", "%" + value + "%");
            });

            options.getEmailAddress().ifPresent(value ->{
                filters.add("email LIKE :email");
                params.put("email", "%" + value + "%");
            });

            options.getPhoneNumber().ifPresent(value ->{
                filters.add("phoneNumber LIKE :phoneNumber");
                params.put("phoneNumber", "%" + value + "%");
            });

            if(!filters.isEmpty()){
                queryString.append(" WHERE ").append(String.join(" AND ", filters));
            }

            if(pageable.getSort().isSorted()){
                Sort.Order order = pageable.getSort().iterator().next();
                queryString.append((" ORDER BY ")).append(order.getProperty()).append(" ").append(order.getDirection().name());
            }

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<User> users = query.getResultList();

            String countQueryStr = queryString.toString().replaceFirst("FROM User", "SELECT COUNT(*) FROM User");
            Query<Long> countQuery = session.createQuery(countQueryStr, Long.class);

            countQuery.setProperties(params);

            long total = countQuery.getSingleResult();

            return new PageImpl<>(users, pageable, total);
        }
    }

    @Override
    public List<User> getAllMvc() {
        try (Session session = sessionFactory.openSession()) {

            Query<User> query = session.createQuery("From User LEFT JOIN FETCH roles", User.class);

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
    public User getUserWithStocks(String username) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.stocks WHERE u.username = :username", User.class);

            query.setParameter("username", username);

            return query.uniqueResult();
        }
    }

    @Override
    public User getUserWithStocksAndWalletsAndInvestments(String username) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u JOIN FETCH u.wallets LEFT JOIN FETCH u.stocks WHERE u.username = :username", User.class);

            query.setParameter("username", username);

            User user = query.uniqueResult();

            Hibernate.initialize(user.getInvestments());

            return user;
        }
    }

    @Override
    public User getUserWithWallets(String username) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.wallets WHERE u.username = :username", User.class);

            query.setParameter("username", username);

            return query.uniqueResult();
        }
    }

    @Override
    public User getUserWithReferrals(String username) {
        try(Session session = sessionFactory.openSession()){

            Query<User> query = session.createQuery("from User u LEFT JOIN FETCH u.referredPeople WHERE u.username = :username", User.class);

            query.setParameter("username", username);

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
    public void delete(User user) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.remove(user);

            session.getTransaction().commit();
        }
    }
}
