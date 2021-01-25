package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Value("${file.upload.dir}")
    private String uploadDir;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void saveUser(User user, MultipartFile file) throws IOException {
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File image = new File(uploadDir, name);
        file.transferTo(image);
        user.setProfilePic(name);
        userRepository.save(user);
    }


    public User getUserById(int id) {

        return userRepository.getOne(id);
    }


    public List<User> getAllUsersByType(Role role) {
        return userRepository.findUserByRole(role);
    }
}
