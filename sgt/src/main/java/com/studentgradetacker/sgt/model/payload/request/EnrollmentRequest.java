package com.studentgradetacker.sgt.model.payload.request;

import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Students;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {

    private Integer studentId;

    private Integer courseId;
}
