package com.mvc.tdd.service.impl;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

// import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.tdd.dao.MathGradesDao;
import com.mvc.tdd.dao.ScienceGradesDao;
import com.mvc.tdd.dao.StudentDao;
import com.mvc.tdd.entity.CollegeStudent;
import com.mvc.tdd.entity.Grade;
import com.mvc.tdd.entity.GradebookCollegeStudent;
import com.mvc.tdd.entity.MathGrade;
import com.mvc.tdd.entity.ScienceGrade;
import com.mvc.tdd.entity.StudentGrades;
import com.mvc.tdd.service.StudentGradeService;

@Service
@Transactional
public class StudentGradeServiceImpl implements StudentGradeService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    // @Autowired
    // @Qualifier("mathGrades")
    // private MathGrade mathGrade;

    // @Autowired
    // @Qualifier("scienceGrades")
    // private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("studentGrades")
    private StudentGrades studentGrade;

    @Override
    public void createStudent(String firstName, String lastName, String email) {
        CollegeStudent newStudent = new CollegeStudent(firstName, lastName, email);
        newStudent.setId(0); // why setId(1) leading to error? and setId(0) doesn't?

        studentDao.save(newStudent);
    }

    @Override
    public boolean studentIsNull(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        if (student.isPresent()) return false;
        else return true;
    }

    @Override
    public void deleteStudentById(int id) {
        if (!studentIsNull(id))  {
            studentDao.deleteById(id);
            mathGradesDao.deleteByStudentId(id);
            scienceGradesDao.deleteByStudentId(id);
        }
        // else studentDao.deleteById(id);
    }

    @Override
    public Iterable<CollegeStudent> getStudentGrades() {
        if (studentDao.findAll() == null) return null;
        return studentDao.findAll();
    }

    @Override
    public boolean createGrade(double grade, int studentId, String subject) {
        // if (studentIsNull(studentId)) return false;
        if (grade >= 0 && grade <= 100) {
            if (subject.equals("math")) {
                MathGrade mathGrade = new MathGrade(grade, studentId);
                mathGrade.setId(0);
                // mathGrade.setGrade(grade);
                // mathGrade.setStudentId(studentId);

                mathGradesDao.save(mathGrade);
                return true;
            }

            if (subject.equals("science")) {
                ScienceGrade scienceGrade = new ScienceGrade(grade, studentId);
                scienceGrade.setId(0);
                // scienceGrade.setGrade(grade);
                // scienceGrade.setStudentId(studentId);

                scienceGradesDao.save(scienceGrade);
                return true;
            }
        }
        return false;
    }

    @Override
    public int deleteGrade(int gradeId, String subject) {
       int studentId = 0;

        if (subject.equals("math")) {
            Optional<MathGrade> mathGrade = mathGradesDao.findById(gradeId);

            if (!mathGrade.isPresent()) return studentId;
            
            studentId = mathGrade.get().getStudentId();
            mathGradesDao.deleteById(gradeId);
        }

        if (subject.equals("science")) {
            Optional<ScienceGrade> scienceGrade = scienceGradesDao.findById(gradeId);

            if (!scienceGrade.isPresent()) return studentId;
            
            studentId = scienceGrade.get().getStudentId();
            scienceGradesDao.deleteById(gradeId);
        }

        return studentId;
    }

    @Override
    public Optional<GradebookCollegeStudent> getStudentDetails(int studentId) {
        Optional<CollegeStudent> student = studentDao.findById(studentId);

        if (!student.isPresent()) return null;

        Iterable<MathGrade> mathGrades = mathGradesDao.findGradesByStudentId(studentId);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradesByStudentId(studentId);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList::add); // method reference feature
        
        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        studentGrade.setMathGradeResults(mathGradeList);
        studentGrade.setScienceGradeResults(scienceGradeList);

        GradebookCollegeStudent gradebookStudent = new GradebookCollegeStudent(studentId, student.get().getFirstName(),
                                                     student.get().getLastName(), student.get().getEmail(), studentGrade);
        
        return Optional.ofNullable(gradebookStudent);
    }
}
