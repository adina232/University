package com.example.university.course.controller;

import com.example.university.course.entity.Course;
import com.example.university.course.service.CourseService;
import com.example.university.student.controller.StudentDto;
import com.example.university.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {
    private final CourseService courseService = mock(CourseService.class);
    private final StudentService studentService = mock(StudentService.class);
    private final CourseController courseController = new CourseController(courseService, studentService);

    @Test
    void showCourseForm() {
        // declar datele
        // rulez programul
        ModelAndView actual = courseController.showCourseForm();

        // verific
        assertEquals("/course/addCourse", actual.getViewName());
        assertTrue(actual.getModel().containsKey("course"));

        Course course = (Course) actual.getModel().get("course");
        assertNull(course.getId());
        assertNull(course.getTitle());
        assertNull(course.getStartingDay());
        assertNull(course.getProfessor());
        assertNull(course.getStudentsEnrolled());
    }


    @Test
    void addCourse() {
        // declar datele
        CourseDto courseDto = new CourseDto();

        // rulez programul
        String result = courseController.addCourse(courseDto);

        // verific
        verify(courseService).addCourse(courseDto);
        assertEquals("redirect:/courseList", result);
    }

    @Test
    void getCourses() {
        // declar datele
        Model model = mock(Model.class);
        List<CourseDto> courseDtoList = new ArrayList<>();
        when(courseService.getCourses()).thenReturn(courseDtoList);

        // rulez programul
        String result = courseController.getCourses(model);

        // verific
        verify(model).addAttribute("courseList", courseDtoList);
        verify(courseService).getCourses();
        assertEquals("course/courseList", result);
    }

    @Test
    void seeStudents() {
        // declar datele
        Integer courseId = 1;
        Model model = mock(Model.class);
        ArrayList<StudentDto> students = new ArrayList<>();

        when(courseService.getStudentsInCourse(courseId)).thenReturn(students);

        // rulez
        String result = courseController.seeStudents(courseId, model);

        // verific
        verify(courseService).getStudentsInCourse(courseId);
        verify(model).addAttribute("courseId", courseId);
        verify(model).addAttribute("studentsInCourse", students);
        assertEquals("course/seeStudents", result);

    }

    @Test
    void deleteStudentFromCourseList() {
        // declar datele
        Integer courseId = 1;
        Integer studentId = 2;
        // rulez
        String result = courseController.deleteStudentFromCourseList(courseId, studentId);

        //verific
        verify(studentService).deleteCourseForStudent(studentId, courseId);
        assertEquals("redirect:/seeStudents?courseId=1", result);
    }
}