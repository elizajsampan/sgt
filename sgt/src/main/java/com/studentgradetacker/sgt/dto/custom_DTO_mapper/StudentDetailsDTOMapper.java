package com.studentgradetacker.sgt.dto.custom_DTO_mapper;

import com.studentgradetacker.sgt.dto.custom_DTO.StudentDetailsDTO;
import com.studentgradetacker.sgt.model.Students;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentDetailsDTOMapper implements Function<Students, StudentDetailsDTO> {

    @Override
    public StudentDetailsDTO apply(Students students) {
        if(students == null) {
            return  null;
        }
        return new StudentDetailsDTO(
                students.getStudentId(),
                students.getFirstName(),
                students.getLastName()
        );
    }
}
