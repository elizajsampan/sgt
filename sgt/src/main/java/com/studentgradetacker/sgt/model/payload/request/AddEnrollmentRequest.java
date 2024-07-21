package com.studentgradetacker.sgt.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {

    private Integer studentId;

    private Integer courseId;
}
