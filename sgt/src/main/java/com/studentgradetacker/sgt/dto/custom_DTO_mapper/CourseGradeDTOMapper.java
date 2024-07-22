package com.studentgradetacker.sgt.dto.custom_DTO_mapper;

import com.studentgradetacker.sgt.dto.custom_DTO.CourseGradeDTO;
import com.studentgradetacker.sgt.enums.GradePoint;
import com.studentgradetacker.sgt.model.Grades;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CourseGradeDTOMapper implements Function<Grades, CourseGradeDTO> {

    @Override
    public CourseGradeDTO apply(Grades grade) {

        return new CourseGradeDTO(
                grade.getEnrolled().getCourses().getCourseCode(),
                grade.getEnrolled().getCourses().getCourseDescription(),
                grade.getPrelims(),
                grade.getMidterms(),
                grade.getFinals(),
                grade.getFinalGrade(),
                grade.getFinalGrade() != null ? GradePoint.fromFinalGrade(grade.getFinalGrade()).getValue() : null,
                grade.getGradeStatus().toString()
        );    }
}
