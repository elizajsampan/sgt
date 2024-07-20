package com.studentgradetacker.sgt.model.payload.request;

import com.studentgradetacker.sgt.model.Courses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEnrolleeCourseRequest {

    private Integer courseId;
}
