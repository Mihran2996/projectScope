package com.example.demo.dto;

import com.example.demo.model.Gender;
import com.example.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private int id;
    @NotBlank(message = "Invalid ` name")
    private String name;
    @NotBlank(message = "Invalid ` surname")
    private String surname;
    @NotBlank(message = "Invalid ` username")
    private String username;
    @Size(min = 6, message = "Invalid ` password,can not be minimum of ` 6")
    private String password;
    @Size(min = 6, message = "Invalid ` confirmPassword,can not be minimum of ` 6")
    private String confirmPassword;
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Invalid ` gender")
    private Gender gender;
    @Enumerated(value = EnumType.STRING)
    private Role role;
}