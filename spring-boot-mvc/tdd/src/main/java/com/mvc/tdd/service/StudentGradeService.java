package com.mvc.tdd.service;

import java.util.Optional;

import com.mvc.tdd.entity.CollegeStudent;
import com.mvc.tdd.entity.GradebookCollegeStudent;

public interface StudentGradeService {
    
    public void createStudent(String firstName, String lastName, String email);
    public boolean studentIsNull(int id);
    public void deleteStudentById(int id);
    // NOTE: when to use Optional as return type (https://www.baeldung.com/java-optional-return)
    public Optional<GradebookCollegeStudent> getStudentDetails(int studentId);
    public Iterable<CollegeStudent>  getStudentGrades();

    public boolean createGrade(double grade, int studentId, String subject);
    public int deleteGrade(int gradeId, String subject);
}
