package com.studentgradetacker.sgt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private Boolean isArchived = Boolean.FALSE;

    public Students(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isArchived = Boolean.FALSE;
    }
}
