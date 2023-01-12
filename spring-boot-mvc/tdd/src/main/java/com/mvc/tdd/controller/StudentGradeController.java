package com.mvc.tdd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mvc.tdd.entity.CollegeStudent;
import com.mvc.tdd.entity.Gradebook;
import com.mvc.tdd.service.StudentGradeService;

@Controller
public class StudentGradeController {
    
    @Autowired
    private StudentGradeService studentGradeService;

    @Autowired
    private Gradebook gradebook;

    @GetMapping(value = "/")
    public String getStudents(Model m) {
        Iterable<CollegeStudent> students = studentGradeService.getStudentGrades();

        m.addAttribute("students", students);

        return "index";
    }

    @GetMapping(value = "studentDetails/{id}")
    public String studentDetails(@PathVariable int id, Model m) {
        return "studentDetails";
    }

    @GetMapping(value = "student/delete/{id}")
    public String deleteStudent(@PathVariable int id, Model m) {
        if (!studentGradeService.studentIsNull(id)) return "error";
        
        studentGradeService.deleteStudentById(id);

        Iterable<CollegeStudent> students = studentGradeService.getStudentGrades();

        m.addAttribute(students);

        return "index";
    }

    @PostMapping(value = "/")
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {
        studentGradeService.createStudent(student.getFirstName(), student.getLastName(), student.getEmail());

        Iterable<CollegeStudent> students = studentGradeService.getStudentGrades();

        m.addAttribute("students", students);

        return "index";
    }
}
