package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Users;
import com.studentgradetacker.sgt.model.payload.request.UserRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sgt")
public class UsersController {

    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<Users> allUsers = usersRepository.findByIsArchivedFalse();

        if (allUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Users not found!"));
        }

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/users/archived")
    public ResponseEntity<?> getAllArchivedUsers() {
        List<Users> allArchivedUsers = usersRepository.findByIsArchivedTrue();

        if (allArchivedUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("There are no archived users"));
        }

        return ResponseEntity.ok(allArchivedUsers);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserByUserId(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User ID is invalid"));
        }

        Users existingUser = usersRepository.findByUserId(id);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found!"));
        }

        return ResponseEntity.ok(existingUser);
    }

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest addUserRequest) {

        if(addUserRequest.getUserName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username cannot be empty"));
        }
        if(addUserRequest.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Password cannot be empty"));
        }
        if(addUserRequest.getRole().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Role cannot be empty"));
        }
        if(addUserRequest.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Firstname cannot be empty"));
        }
        if(addUserRequest.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Lastname cannot be empty"));
        }

        Users user;
        if (addUserRequest.getMiddleName() != null && !addUserRequest.getMiddleName().isEmpty()) {
            user = new Users(
                    addUserRequest.getUserName(),
                    addUserRequest.getPassword(),
                    addUserRequest.getRole(),
                    addUserRequest.getFirstName(),
                    addUserRequest.getMiddleName(),
                    addUserRequest.getLastName()
            );
        } else {
            user = new Users(
                    addUserRequest.getUserName(),
                    addUserRequest.getPassword(),
                    addUserRequest.getRole(),
                    addUserRequest.getFirstName(),
                    addUserRequest.getLastName()
            );
        }
        usersRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User has been added successfully!"));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserRequest updateUserRequest, @PathVariable Integer id) {
        Optional<Users> userOptional = Optional.ofNullable(usersRepository.findByUserId(id));
        System.out.println(userOptional);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User ID is invalid"));
        }
        if(updateUserRequest.getUserName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username cannot be empty"));
        }
        if(updateUserRequest.getRole().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Role cannot be empty"));
        }
        if(updateUserRequest.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Password cannot be empty"));
        }
        if(updateUserRequest.getFirstName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Firstname cannot be empty"));
        }
        if(updateUserRequest.getLastName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Lastname cannot be empty"));
        }

        Users existingUser = userOptional.get();

        existingUser.setUserName(updateUserRequest.getUserName());
        existingUser.setPassword(updateUserRequest.getPassword());
        existingUser.setRole(updateUserRequest.getRole());
        existingUser.setFirstName(updateUserRequest.getFirstName());
        existingUser.setMiddleName(updateUserRequest.getMiddleName());
        existingUser.setLastName(updateUserRequest.getLastName());

        usersRepository.save(existingUser);

        return ResponseEntity.ok(new MessageResponse("User details updated successfully!"));
    }

    @PutMapping("/user/archive/{id}")
    public ResponseEntity<?> archiveUser(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User ID is invalid!"));
        }

        Users existingUser = usersRepository.findByUserId(id);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found!"));
        }

        existingUser.setIsArchived(Boolean.TRUE);

        usersRepository.save(existingUser);

        return ResponseEntity.ok(new MessageResponse("User has been archived!"));
    }
}