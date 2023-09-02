package com.example.university.student.controller;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.student.entity.Student;
import com.example.university.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    private final StudentService studentService = mock(StudentService.class);
    private final StudentController studentController = new StudentController(studentService);

    @Test
    void home() {
        // data

        // run
        String result = studentController.home();
        // verify
        assertEquals("home", result);
    }

    @Test
    void showStudentForm() {
        // data

        // run
        ModelAndView result = studentController.showStudentForm();
        // verify
        assertTrue(result.getModel().containsKey("student"));
        Student student = (Student) result.getModel().get("student");
        assertNull(student.getId());
        assertNull(student.getDateOfBirth());
        assertNull(student.getFirstName());
        assertNull(student.getLastName());
        assertNull(student.getCourses());

        assertEquals("students/addStudent", result.getViewName());
    }

    @Test
    void addStudent() {
        // data
        StudentDto studentDto = new StudentDto();
        studentDto.setDateOfBirth("2000-01-01");
        studentDto.setCourses(new ArrayList<>());
        // run
        String result = studentController.addStudent(studentDto);

        // verify
        verify(studentService).addStudent(studentDto);
        assertEquals("redirect:/studentList", result);

    }

    @Test
    void studentList() {
        // data
        Model model = mock(Model.class);
        List<StudentDto> studentDtoList = new ArrayList<>();
        when(studentService.getStudents()).thenReturn(studentDtoList);
        // run
        String result = studentController.studentList(model);
        // verify
        verify(model).addAttribute("studentList", studentDtoList);
        assertEquals("students/studentList", result);
    }

    @Test
    void studentsAddCourses() {
        // data
        Student student = new Student();
        student.setId(1);
        student.setDateOfBirth(getDate());
        student.setCourses(new ArrayList<>());

        List<CourseDto> courseList = new ArrayList<>();
        Model model = mock(Model.class);
        when(studentService.studentsAddCourses(student.getId())).thenReturn(courseList);
        // run
        String result = studentController.studentsAddCourses(student.getId(), model);
        // verify
        verify(model).addAttribute("courseListDto", courseList);
        verify(model).addAttribute("studentId", 1);
        assertEquals("students/studentsAddCourses", result);

    }


    @Test
    void studentsSeeCourses() {
        // data
        Student student = new Student();
        student.setId(1);
        Date date = getDate();
        student.setDateOfBirth(date);
        student.setCourses(new ArrayList<>());

        Model model = mock(Model.class);

        List<CourseDto> courseDtoList = new ArrayList<>();
        when(studentService.studentsSeeCourses(student.getId())).thenReturn(courseDtoList);
        // run
        String result = studentController.studentsSeeCourses(student.getId(), model);
        // verify
        verify(model).addAttribute("courses", courseDtoList);
        verify(model).addAttribute("studentId", 1);
        assertEquals("students/studentsSeeCourses", result);

    }

    @Test
    void deleteCourseForStudent() {
        // data
        Student student = new Student();
        student.setId(1);
        Date date = getDate();
        student.setDateOfBirth(date);

        Course course = new Course();
        course.setId(10);
        course.setStartingDay(date);
        List<Course> courseList = new ArrayList<>();
        courseList.add(course);

        student.setCourses(courseList);
        // run
        String result = studentController.deleteCourseForStudent(1, 10);

        // verify
        verify(studentService).deleteCourseForStudent(1, 10);
        assertEquals("redirect:/studentsSeeCourses?id=1", result);

    }

    @Test
    void addCourseToStudent() {
        // data
        Student student = new Student();
        student.setId(1);
        Date date = getDate();
        student.setDateOfBirth(date);

        Course course = new Course();
        course.setId(10);
        course.setStartingDay(date);

        // run
        String result = studentController.addCourseToStudent(1, 10);
        // verify
        verify(studentService).addCourseToStudent(1, 10);
        assertEquals("redirect:/studentsAddCourses?id=1", result);
    }

    private Date getDate() {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}