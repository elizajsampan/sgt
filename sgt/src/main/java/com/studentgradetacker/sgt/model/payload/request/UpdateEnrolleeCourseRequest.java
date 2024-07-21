package com.studentgradetacker.sgt.model.payload.request;

import com.studentgradetacker.sgt.model.Courses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEnrolleeCourseRequest {

    private Integer courseId;
}
