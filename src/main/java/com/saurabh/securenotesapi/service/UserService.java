package com.saurabh.securenotesapi.service;

import com.saurabh.securenotesapi.dto.UserDTO;
import com.saurabh.securenotesapi.model.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    User findByUsername(String username);

    List<User> getAllUsers();
    UserDTO getUserById(Long id);
}
