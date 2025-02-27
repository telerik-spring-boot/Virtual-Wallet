package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.role.RoleRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {

        User user = userRepository.getById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", "id", id);
        }

        return user;
    }

    @Override
    public void create(User user) {
        List<User> dbUsers = userRepository.getByAnyUniqueField(user.getUsername(), user.getEmail(), user.getPhoneNumber());

        if(!dbUsers.isEmpty()){
            appropriateThrow(user, dbUsers.get(0));
        }

        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        List<User> dbUsers = userRepository.getByAnyUniqueField(user.getUsername(), user.getEmail(), user.getPhoneNumber());

        if(!dbUsers.isEmpty()){
            dbUsers.stream()
                    .filter(dbUser -> user.getId() != dbUser.getId())
                    .findFirst()
                    .ifPresent(dbUser -> appropriateThrow(user, dbUser));
        }


        userRepository.update(user);

    }

    @Override
    public void delete(int id) {

        User userToDelete = userRepository.getById(id);

        if (userToDelete == null) {
            throw new EntityNotFoundException("User", "id", id);
        }

        userRepository.delete(id);

    }

    private static void appropriateThrow(User user, User dbUser) {

        if(dbUser.getEmail().equalsIgnoreCase(user.getEmail())){
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        if(dbUser.getUsername().equalsIgnoreCase(user.getUsername())){
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
        if(dbUser.getPhoneNumber().equalsIgnoreCase(user.getPhoneNumber())){
            throw new DuplicateEntityException("User", "Phone Number", user.getPhoneNumber());
        }

    }
}
