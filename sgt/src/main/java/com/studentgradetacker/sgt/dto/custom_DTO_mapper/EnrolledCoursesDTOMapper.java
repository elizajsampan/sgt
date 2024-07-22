package com.studentgradetacker.sgt.dto.custom_DTO_mapper;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledCoursesDTO;
import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Enrolled;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EnrolledCoursesDTOMapper implements Function<Courses, EnrolledCoursesDTO> {

    @Override
    public EnrolledCoursesDTO apply(Courses enrolledCourses) {
        if(enrolledCourses == null ) {

            return null;
        }

        return new EnrolledCoursesDTO(
                enrolledCourses.getCourseCode(),
                enrolledCourses.getCourseDescription(),
                enrolledCourses.getUnits()
        );
    }
}
