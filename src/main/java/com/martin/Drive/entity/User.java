package com.martin.Drive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String username;
    private String roles;
    private String password;
    private String nombre;
    private int puntuacion;

    @OneToMany(fetch = FetchType.LAZY,cascade={CascadeType.ALL})
    private List<UserMessages> userMessagesList;
}
