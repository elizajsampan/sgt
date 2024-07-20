package com.studentgradetacker.sgt.dto.custom_DTO_mapper;

import com.studentgradetacker.sgt.dto.custom_DTO.StudentGradesDTO;
import com.studentgradetacker.sgt.model.Grades;
import com.studentgradetacker.sgt.model.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentGradesDTOMapper implements Function<Grades, StudentGradesDTO> {
    @Override
    public StudentGradesDTO apply(Grades grades) {
        if (grades == null || grades.getEnrolled() == null) {
            return null;
        }

        return new StudentGradesDTO(
                grades.getEnrolled().getStudents().getFirstName(),
                grades.getEnrolled().getStudents().getLastName(),
                grades.getEnrolled().getCourses().getCourseCode(),
                grades.getEnrolled().getCourses().getCourseDescription(),
                grades.getPrelims(),
                grades.getMidterms(),
                grades.getFinals(),
                grades.getFinalGrade()
        );
    }
}
