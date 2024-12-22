package com.example.bookrent.Service;


import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    public User findUserByProfile(String jwt) throws UserNotFoundException;
    public User findUserById(Long id) throws UserNotFoundException;
    public List<User> getAllUsers();
}
