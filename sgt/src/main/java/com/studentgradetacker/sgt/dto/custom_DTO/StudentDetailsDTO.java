package com.studentgradetacker.sgt.dto.custom_DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetailsDTO {

    private Integer studentId;

    private String firstName;

    private String lastName;
}
