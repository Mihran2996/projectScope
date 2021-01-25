package com.example.demo.controller;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import com.example.demo.util.TextUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectService projectService;
    @Value("${file.upload.dir}")
    private String uploadDir;

    @PostMapping("/add/user")
    public String addUser(@Valid @ModelAttribute(value = "userRequest") UserRequestDto userRequestDto, BindingResult bindingResult,
                          @RequestParam("image") MultipartFile file, ModelMap modelMap) throws IOException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        Optional<User> byUserName = userRepository.findByUsername(userRequestDto.getUsername());
        if (!TextUtil.VALID_EMAIL_ADDRESS_REGEX.matcher(userRequestDto.getUsername()).find()) {
            modelMap.addAttribute("msg", "Email does not valid!");
            return "register";
        }
        if (byUserName.isPresent()) {
            modelMap.addAttribute("msg", "User does not exists");
            return "register";
        }
        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
            modelMap.addAttribute("msg", "");
            return "register";
        }
        User user = User.builder()
                .name(userRequestDto.getName())
                .surname(userRequestDto.getSurname())
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .gender(userRequestDto.getGender())
                .role(userRequestDto.getRole())
                .build();
        userService.saveUser(user, file);
        modelMap.addAttribute("msg2", "User was added");
        return "register";
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("name") String imageName) throws IOException {
        InputStream in = new FileInputStream(uploadDir + File.separator + imageName);
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/leader/page")
    public String leaderPage(ModelMap modelMap, @AuthenticationPrincipal CurrentUser user) {
        modelMap.addAttribute("users", userService.getAllUsersByType(Role.TEAM_MEMBER));
        modelMap.addAttribute("user", userService.getUserById(user.getUser().getId()));
        return "leader";
    }

    @GetMapping("/member/page")
    public String memberPage(ModelMap modelMap, @AuthenticationPrincipal CurrentUser user) {
        modelMap.addAttribute("projects", projectService.getAllProjectsById(user.getUser().getId()));
        modelMap.addAttribute("users", userService.getAllUsersByType(Role.TEAM_MEMBER));
        modelMap.addAttribute("user", userService.getUserById(user.getUser().getId()));
        return "member";
    }
}
