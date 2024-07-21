package com.studentgradetacker.sgt.controller;

import com.studentgradetacker.sgt.model.Users;
import com.studentgradetacker.sgt.model.payload.request.UserRequest;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import com.studentgradetacker.sgt.respository.UsersRepository;
import com.studentgradetacker.sgt.util.PasswordUtil;
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
        String encodedPassword = PasswordUtil.encodePassword(addUserRequest.getPassword());
        String lowerCaseUserName = addUserRequest.getUserName().toLowerCase();
        String role = String.valueOf(addUserRequest.getRole()).toUpperCase();

        Optional<Users> existingUser = Optional.ofNullable(usersRepository.findByUserName(lowerCaseUserName));
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username already exists"));
        }
        if(lowerCaseUserName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username cannot be empty"));
        }
        if(addUserRequest.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Password cannot be empty"));
        }
        if (!addUserRequest.getPassword().equals(addUserRequest.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Passwords do not match"));
        }
        if(addUserRequest.getRole().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Role cannot be empty"));
        }
        if (!role.equals("ADMIN") && !role.equals("TEACHER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Role is invalid"));
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
                    lowerCaseUserName,
                    encodedPassword,
                    role,
                    addUserRequest.getFirstName(),
                    addUserRequest.getMiddleName(),
                    addUserRequest.getLastName()
            );
        } else {
            user = new Users(
                    lowerCaseUserName,
                    encodedPassword,
                    role,
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

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User ID is invalid"));
        }

        Users user = userOptional.get();
        if (updateUserRequest.getUserName() != null && !updateUserRequest.getUserName().isEmpty()) {
            String lowerCaseUserName = updateUserRequest.getUserName().toLowerCase();
            Optional<Users> existingUser = Optional.ofNullable(usersRepository.findByUserName(lowerCaseUserName));
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username already exists"));
            }
            user.setUserName(lowerCaseUserName);
        }

        if (updateUserRequest.getRole() != null) {
            String role = String.valueOf(updateUserRequest.getRole()).toUpperCase();
            if (!role.equals("ADMIN") && !role.equals("TEACHER")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Role is invalid"));
            }
            user.setRole(role);
        }

        if (updateUserRequest.getFirstName() != null && !updateUserRequest.getFirstName().isEmpty()) {
            user.setFirstName(updateUserRequest.getFirstName());
        }

        if (updateUserRequest.getLastName() != null && !updateUserRequest.getLastName().isEmpty()) {
            user.setLastName(updateUserRequest.getLastName());
        }

        if (updateUserRequest.getMiddleName() != null) {
            user.setMiddleName(updateUserRequest.getMiddleName());
        }

//        if(updateUserRequest.getUserName().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username cannot be empty"));
//        }
//        if(updateUserRequest.getRole().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Role cannot be empty"));
//        }
//        if(updateUserRequest.getPassword().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Password cannot be empty"));
//        }
//        if(updateUserRequest.getFirstName().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Firstname cannot be empty"));
//        }
//        if(updateUserRequest.getLastName().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Lastname cannot be empty"));
//        }

//        user.setUserName(updateUserRequest.getUserName());
//        user.setPassword(updateUserRequest.getPassword());
//        user.setRole(updateUserRequest.getRole());
//        user.setFirstName(updateUserRequest.getFirstName());
//        user.setMiddleName(updateUserRequest.getMiddleName());
//        user.setLastName(updateUserRequest.getLastName());

        usersRepository.save(user);

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