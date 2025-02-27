package com.telerik.virtualwallet.services.user;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.models.User;
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
    public User getById(int id) {

        User user = userRepository.getById(id);

        if (user == null) {
            throw new EntityNotFoundException("User", "id", id);
        }

        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.getByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }

        return user;
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        User user = userRepository.getByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new EntityNotFoundException("User", "phoneNumber", phoneNumber);
        }

        return user;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.getByUsername(username);

        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
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
            for(User dbUser : dbUsers){
                if(dbUser.getId() == user.getId()){
                    if(!user.getUsername().equals(dbUser.getUsername()))
                        throw new UnauthorizedOperationException("Username modification is not allowed.");
                }else appropriateThrow(user, dbUser);
            }
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
