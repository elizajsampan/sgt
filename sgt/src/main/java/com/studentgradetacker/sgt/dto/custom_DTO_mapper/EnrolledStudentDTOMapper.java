package com.studentgradetacker.sgt.dto.custom_DTO_mapper;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledStudentDTO;
import com.studentgradetacker.sgt.model.Enrolled;

import java.util.function.Function;

public class EnrolledStudentDTOMapper implements Function<Enrolled, EnrolledStudentDTO> {
    @Override
    public EnrolledStudentDTO apply(Enrolled enrolled) {
        if(enrolled == null ) {

            return null;
        }

        return new EnrolledStudentDTO(
                enrolled.getStudents().getStudentId(),
                enrolled.getCourses().getCourseDescription(),
                enrolled.getCourses().getCourseCode(),
                enrolled.getCourses().getUnits()
        );
    }
}
