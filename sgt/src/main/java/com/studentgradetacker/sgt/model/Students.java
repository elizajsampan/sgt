package com.studentgradetacker.sgt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Students {

    @Id
    private Integer studentId;

    private String firstName;

    private String lastName;

    private String email;
}
