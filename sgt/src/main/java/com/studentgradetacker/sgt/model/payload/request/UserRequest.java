package com.studentgradetacker.sgt.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {

    private String userName;

    private String password;

    private String confirmPassword;

    private String role;

    private String firstName;

    private String middleName;

    private String lastName;
}
