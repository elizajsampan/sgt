package com.studentgradetacker.sgt.service;

import com.studentgradetacker.sgt.enums.GradePoint;
import com.studentgradetacker.sgt.model.Courses;
import com.studentgradetacker.sgt.model.Enrolled;
import com.studentgradetacker.sgt.model.Grades;
import com.studentgradetacker.sgt.model.Students;
import com.studentgradetacker.sgt.repository.CoursesRepository;
import com.studentgradetacker.sgt.repository.EnrolledRepository;
import com.studentgradetacker.sgt.repository.GradesRepository;
import com.studentgradetacker.sgt.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsService {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private EnrolledRepository enrolledRepository;

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    public Double calculateGWA(Integer studentId) {
        List<Grades> gradesList = gradesRepository.findByStudentId(studentId);
        double totalGradePoints = 0.0;
        int totalUnits = 0;

        for (Grades grade : gradesList) {
            if (grade.getFinalGrade() != null) {
                Double finalGrade = grade.getFinalGrade();
                double gradePointValue = GradePoint.fromFinalGrade(finalGrade).getValue();

                // Assuming you have a way to get the associated course units
                Courses course = getCourseByEnrolledId(grade.getEnrolled().getEnrolledId());
                if (course != null) {
                    int units = course.getUnits();
                    totalGradePoints += gradePointValue * units;
                    totalUnits += units;
                }
            }
        }

        if (totalUnits > 0) {
            double rawGWA = totalGradePoints / totalUnits;
            return Math.round(rawGWA * 100.0) / 100.0;
        } else {
            return null;
        }
    }

    private Courses getCourseByEnrolledId(Integer enrolledId) {

        return coursesRepository.findByEnrolledId(enrolledId);
    }
}