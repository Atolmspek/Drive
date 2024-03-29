package com.martin.Drive.controller;

import com.martin.Drive.entity.AuthRequest;
import com.martin.Drive.entity.User;
import com.martin.Drive.service.JwtService;
import com.martin.Drive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @CrossOrigin()
    @PostMapping("/loginUser")
    public String addUser(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){

            return jwtService.generateToken(authRequest.getUsername());
        }else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }


    @PostMapping("/addUser")
    @CrossOrigin()
    public String addUser(@RequestBody User userInfo){
        return userService.addUser(userInfo);

    }

    @PatchMapping("/updateUserData/{id}")
    @CrossOrigin()
    public ResponseEntity <String> updateUser (@PathVariable Long id,  @RequestParam("newEmail") String newEmail,
                                               @RequestParam("newPhone") int newPhone,
                                               @RequestParam("newBio") String newBio) {
        User userToBeUpdated=userService.findUser(id);

        userToBeUpdated.setBio(newBio);
        userToBeUpdated.setMovil(newPhone);
        userToBeUpdated.setEmail(newEmail);
        userService.saveUser(userToBeUpdated);
        return ResponseEntity.ok("Usuario actualizado con éxito");
    }



}
