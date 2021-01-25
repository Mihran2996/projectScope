package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    @Transient
    private String confirmPassword;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Project> projects;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private String profilePic;

}
