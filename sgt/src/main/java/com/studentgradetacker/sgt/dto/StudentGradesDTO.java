package com.studentgradetacker.sgt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGradesDTO {

        private String firstName;
        private String lastName;
        private Double prelims;
        private Double midterms;
        private Double finals;
        private Double finalGrade;

//        public StudentGradesDTO(String firstName, String lastName, Double prelims, Double midterms, Double finals, Double finalGrade) {
//            this.firstName = firstName;
//            this.lastName = lastName;
//            this.prelims = prelims;
//            this.midterms = midterms;
//            this.finals = finals;
//            this.finalGrade = finalGrade;
//        }

}
