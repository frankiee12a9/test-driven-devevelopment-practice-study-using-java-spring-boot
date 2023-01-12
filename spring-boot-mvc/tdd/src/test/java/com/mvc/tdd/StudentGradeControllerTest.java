package com.mvc.tdd;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  // not work
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // work
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import com.mvc.tdd.dao.StudentDao;
import com.mvc.tdd.entity.CollegeStudent;
import com.mvc.tdd.entity.GradebookCollegeStudent;
import com.mvc.tdd.service.StudentGradeService;

// @TestPropertySource("/application.properties")
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class StudentGradeControllerTest {

    private static MockHttpServletRequest httpRequest;
    
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentGradeService studentServiceMock;

    @Autowired
    private StudentDao studentDao;

    @Value("${sql.script.create.student}") 
    private String sqlInsertStudentData;

    @Value("${sql.script.create.math.grade}") 
    private String sqlInsertMathGradeData;

    @Value("${sql.script.create.science.grade}") 
    private String sqlInsertScienceGradeData;

    @Value("${sql.script.delete.student}") 
    private String sqlDeleteStudentData;

    @Value("${sql.script.delete.math.grade}") 
    private String sqlDeleteMathGradeData;

    @Value("${sql.script.delete.science.grade}") 
    private String sqlDeleteScienceGradeData;

    @BeforeAll 
    public static void setup() {
        httpRequest = new MockHttpServletRequest();

        httpRequest.setParameter("firstName", "jane");
        httpRequest.setParameter("lastName", "nguyen");
        httpRequest.setParameter("email", "mail1@test.com");
    }

    @BeforeEach
    public void beforeEach() {
        jdbc.execute(sqlInsertStudentData);
    }

    @Test
    void getStudentsHttpRequest() throws Exception {
        CollegeStudent studentA = new GradebookCollegeStudent("tom", "nguyen", "mail1@test.com");
        CollegeStudent studentB = new GradebookCollegeStudent("cat", "nguyen", "mail2@test.com");

        List<CollegeStudent> students = new ArrayList<>(Arrays.asList(studentA, studentB));

        when(studentServiceMock.getStudentGrades()).thenReturn(students);

        assertIterableEquals(students, studentServiceMock.getStudentGrades());

        MvcResult mvcResult = mockMvc.perform(get("/"))
                                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index"); // "index" is returned page from corresponding controller
    }

    @Test
    void createStudentHttpRequest() throws Exception {
        // test updating for UI (add/get students)
        // CollegeStudent studentA = new CollegeStudent("tom", "nguyen", "tom@test.com");
        // List<CollegeStudent> students = new ArrayList<>(Arrays.asList(studentA));
        // when(studentServiceMock.getGradebook()).thenReturn(students);
        // assertIterableEquals(students, studentServiceMock.getGradebook());

        MvcResult mvcResult = mockMvc.perform(post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("firstName", httpRequest.getParameterValues("firstName"))
                                .param("lastName", httpRequest.getParameterValues("lastName"))
                                .param("email", httpRequest.getParameterValues("email")))
                                .andExpect(status().isOk()).andReturn();

        // check view
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");

        // verify provided view model with database
        CollegeStudent studentToVerify = studentDao.findByEmail("mail1@test.com");
        
        assertNotNull(studentToVerify, "Student should not be null");
        // assertNull(studentToVerify, "Student should be null");
    }

    @Test
    void deleteStudentHttpRequest() throws Exception {
        int studentId = 2;
        assertTrue(studentDao.findById(studentId).isPresent(), "Find student by Id (should return true)");

        MvcResult mvcResult = mockMvc.perform(get("/student/delete/{id}", studentId))
                                    .andExpect(status().isOk())
                                    .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        assertFalse(studentDao.findById(studentId).isPresent());
    }

    @Test
    void deleteStudentHttpRequestWithErrorPage() throws Exception {
        int nonExistingStudentId = 111;
        MvcResult mvcResult = mockMvc.perform(get("/student/delete/{id}", nonExistingStudentId))
                                    .andExpect(status().isOk())
                                    .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test 
    void getStudentDetailsHttpRequest() throws Exception {
        int studentId = 2;
        assertTrue(studentDao.findById(studentId).isPresent());

        MvcResult mvcResult = mockMvc.perform(get("/studentDetails/{id}", studentId))
                                    .andExpect(status().isOk())
                                    .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "studentDetails");
    }

    @Test 
    void getStudentDetailsHttpRequestWithErrorPage() throws Exception {
        int studentId = 222;
        assertFalse(studentDao.findById(studentId).isPresent());

        MvcResult mvcResult = mockMvc.perform(get("/studentDetails/{id}", studentId))
                                    .andExpect(status().isOk())
                                    .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");
    }
    @AfterEach
    public void afterEach() {
        jdbc.execute(sqlDeleteStudentData);
    }
}
