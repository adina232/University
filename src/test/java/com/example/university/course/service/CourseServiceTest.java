package com.example.university.course.service;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.course.repository.CourseRepository;
import com.example.university.student.controller.StudentDto;
import com.example.university.student.entity.Student;
import com.example.university.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final CourseService courseService = new CourseService(courseRepository, studentRepository);

    @Test
    void addCourse() {
        // declar datele
        CourseDto courseDto = new CourseDto();
        courseDto.setId(1);
        courseDto.setTitle("Test");
        courseDto.setStartingDay("2000-01-01");

        Course course = new Course();
        course.setId(1);
        course.setTitle("Test");
        course.setStartingDay(getDate(courseDto.getStartingDay()));

        // rulez programul
        courseService.addCourse(courseDto);

        // verific
        verify(courseRepository).save(course);
    }

    @Test
    void getCourses() {
        // declar datele
        Course course = new Course();
        course.setId(1);
        course.setTitle("Test");
        course.setStartingDay(getDate("2000-01-01"));

        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));

        // rulez programul
        List<CourseDto> resultList = courseService.getCourses();

        // verific
        assertEquals(1, resultList.get(0).getId());
        assertEquals("Test", resultList.get(0).getTitle());
        assertEquals("2000-01-01", resultList.get(0).getStartingDay());
    }

    @Test
    void getStudentsInCourse() {
        // declar datele
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setId(10);
        student.setFirstName("Test");
        student.setDateOfBirth(getDate("2000-01-01"));
        studentList.add(student);

        Course course = new Course();
        course.setId(1);
        course.setTitle("Test");
        course.setStartingDay(getDate("2000-01-01"));
        course.setStudentsEnrolled(studentList);

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        // rulez programul
        List<StudentDto> resultList = courseService.getStudentsInCourse(course.getId());
        // verific
        assertEquals(10, resultList.get(0).getId());
        assertEquals("Test", resultList.get(0).getFirstName());
        assertEquals("2000-01-01", resultList.get(0).getDateOfBirth());
    }

    private Date getDate(String stringDate) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}