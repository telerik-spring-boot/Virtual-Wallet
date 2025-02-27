package com.telerik.virtualwallet.repositories.role;

import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.Role;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final SessionFactory sessionFactory;


    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Role getRoleByName(String name) {
        try(Session session = sessionFactory.openSession()){

            Query<Role> query = session.createQuery("from Role where roleName = :name", Role.class);

            query.setParameter("name", name);

            return query.uniqueResult();
        }
    }
}
