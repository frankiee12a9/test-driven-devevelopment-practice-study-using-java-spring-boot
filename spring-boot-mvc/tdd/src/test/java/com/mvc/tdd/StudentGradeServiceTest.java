package com.mvc.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.mvc.tdd.dao.MathGradesDao;
import com.mvc.tdd.dao.ScienceGradesDao;
import com.mvc.tdd.dao.StudentDao;
import com.mvc.tdd.entity.CollegeStudent;
import com.mvc.tdd.entity.GradebookCollegeStudent;
import com.mvc.tdd.entity.MathGrade;
import com.mvc.tdd.entity.ScienceGrade;
import com.mvc.tdd.service.StudentGradeService;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentGradeServiceTest {
 
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private StudentGradeService studentGradeService;

    @Autowired
    private JdbcTemplate jdbc;


    @BeforeEach
    void setupBeforeTransactions() {
        jdbc.execute("insert into student(id, first_name, last_name, email_address) " +
         "values(2, 'kane', 'nguyen', 'cudayanh@test.com')");

        // NOTE: error if concurrently execute this with `createGradeService`
        jdbc.execute("insert into math_grade(id, student_id, grade) values(2, 2, 60.00)");
        jdbc.execute("insert into science_grade(id, student_id, grade) values(2, 2, 80.00)");
    }

    // STUDENT
    @Test
    void createStudentServicePassed() throws Exception {
        studentGradeService.createStudent("user", "nguyen1", "user@test.com");

        String email = "user@test.com";
        CollegeStudent student = studentDao.findByEmail(email);

        assertEquals(email, student.getEmail(), "find existing student by email");
    }

    @Test
    void isStudentNullCheck() throws Exception {
        int nonExistingStudentId = 1;
        int existingStudentId = 2;

        assertTrue(studentGradeService.studentIsNull(nonExistingStudentId), "check non existed student(should return true)");

        assertFalse(studentGradeService.studentIsNull(existingStudentId), "check existing student(should return false)");
    }

    @Test
    void deleteStudent()  throws Exception {
        // int existingStudentId = 1;
        // int existingStudentId = 0;
        int existingStudentId = 2;

        Optional<CollegeStudent> student = studentDao.findById(existingStudentId);

        assertTrue(student.isPresent(), "Delete existing student");

        studentGradeService.deleteStudentById(existingStudentId);

        student = studentDao.findById(existingStudentId);

        assertFalse(student.isPresent(), "Delete non existing student");
    }

    @Sql("/insertData.sql") // NOTE: `insertData.sql` being executed before @beforeEach
    @Test
    void getStudentGradesPassed() throws Exception {
        Iterable<CollegeStudent> iterableStudents = studentGradeService.getStudentGrades();
        List<CollegeStudent> students = new ArrayList<>();

        for (var aStudent : iterableStudents) {
            students.add(aStudent);
        }

        assertEquals(4, students.size(), "Should passed");
    }

    @Test
    void getStudentDetails() throws Exception {
        // get student by studentId
        Optional<GradebookCollegeStudent> student = studentGradeService.getStudentDetails(2);

        // verify that student is present 
        assertNotNull(student, "Student should be present");

        // verify student details(firstName, lastName, email, etc..)
        assertEquals(2, student.get().getId());
        assertEquals("kane", student.get().getFirstName());
        assertEquals("nguyen", student.get().getLastName());
        assertEquals("cudayanh@test.com", student.get().getEmail());

        // verify grades current student has so far
        assertTrue(student.get().getStudentGrades().getMathGradeResults().size() >= 1);
        assertTrue(student.get().getStudentGrades().getScienceGradeResults().size() >= 1);
    }

    @Test
    void getStudentDetailsNotFound() throws Exception {
        Optional<GradebookCollegeStudent> student = studentGradeService.getStudentDetails(000);
        assertNull(student);
    }


    // GRADE
    @Test
    void createGradesService() throws Exception {
        int studentId = 2;

        // create the grades
        assertTrue(studentGradeService.createGrade(50.50, studentId, "math"));
        assertTrue(studentGradeService.createGrade(50.60, studentId, "science"));

        // get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradesByStudentId(studentId);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradesByStudentId(studentId);

        // verify there are grades
        assertTrue(mathGrades.iterator().hasNext(), "Student should has math grades");
        assertTrue(scienceGrades.iterator().hasNext(), "Student should has science grades");

        // verify number of each specific grades
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() >= 1, "Student should has >= 1 science grades");
        assertTrue(((Collection<MathGrade>) mathGrades).size() >= 1, "Student should has >= 1 science grades");
    }

    @Test
    void deleteGradeService() throws Exception {
        assertEquals(2, studentGradeService.deleteGrade(2, "math"), "Should return studentId after delete");
        assertEquals(2, studentGradeService.deleteGrade(2, "science"), "Should return studentId after delete");
    }

    @Test
    void deleteGradeServiceEnhancements() throws Exception {
        // get grades
        Optional<MathGrade> mathGrade = mathGradeDao.findById(2);
        Optional<ScienceGrade> scienceGrade = scienceGradeDao.findById(2);

        // check if grades are present
        assertTrue(mathGrade.isPresent(), "math grade should be present");
        assertTrue(scienceGrade.isPresent(), "science grade should be present");

        // delete grades
        studentGradeService.deleteGrade(1, "math");
        studentGradeService.deleteGrade(2, "science");

        // get grades again
        mathGrade = mathGradeDao.findById(1);
        scienceGrade = scienceGradeDao.findById(2);

        // verify all deleted grades aren't present anymore
        assertFalse(scienceGrade.isPresent(), "science grade shouldn't be present");
        assertFalse(scienceGrade.isPresent(), "science grade shouldn't be present");
    }

    @Test
    void deleteGradeServiceWithNonExistingStudentId() throws Exception {
        assertNotEquals(0, studentGradeService.deleteGrade(2, "math"), "Shouldn't return studentId after delete");
        assertNotEquals(0, studentGradeService.deleteGrade(2, "science"), "Shouldn't return studentId after delete");
    }

    @Test
    void createGradesServiceReturnFalse() throws Exception {
        int studentId = 0;

        // create the invalid grades
        assertFalse(studentGradeService.createGrade(-1, studentId, "math"));
        assertFalse(studentGradeService.createGrade(120, studentId, "science"));
        assertFalse(studentGradeService.createGrade(100, 2, "physics"));
    }

    @AfterEach
    public void setupAfterTransactions()  throws Exception {
        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
    }
}
