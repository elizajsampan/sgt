package com.studentgradetacker.sgt.dto.custom_DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class StudentGradesDTO implements Serializable {

        private String firstName;
        private String lastName;
        private String courseDescription;
        private String courseCode;
        private Double prelims;
        private Double midterms;
        private Double finals;
        private Double finalGrade;

        public StudentGradesDTO(String firstName, String lastName, String courseDescription, String courseCode, Double prelims, Double midterms, Double finals, Double finalGrade) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.courseDescription = courseDescription;
                this.courseCode = courseCode;
                this.prelims = prelims;
                this.midterms = midterms;
                this.finals = finals;
                this.finalGrade = finalGrade;
        }

}
