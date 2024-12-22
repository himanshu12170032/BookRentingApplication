package com.example.bookrent.Service;

import com.example.bookrent.Config.JwtProvider;
import com.example.bookrent.Entity.User;
import com.example.bookrent.Exception.UserNotFoundException;
import com.example.bookrent.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User findUserByProfile(String jwt) throws UserNotFoundException {
        String email = jwtProvider.getEmailFromToken(jwt);
        if(email == null){
            throw new BadCredentialsException("Invalid token received");
        }
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("User not found with email "+email);
        }
        return user;
    }

    @Override
    public User findUserById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserNotFoundException("user not found with id "+id);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

}
