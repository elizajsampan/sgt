package com.studentgradetacker.sgt.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    private String courseDescription;

    private String courseCode;

    private Integer units;
}
