package com.example.university.student.service;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.course.repository.CourseRepository;
import com.example.university.student.controller.StudentDto;
import com.example.university.student.entity.Student;
import com.example.university.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final StudentService studentService = new StudentService(studentRepository, courseRepository);

    @Test
    void addStudent() {
        // data
        StudentDto studentDto = new StudentDto();
        studentDto.setDateOfBirth("1999-01-01");


        Student student = new Student();
        student.setDateOfBirth(getDate("1999-01-01"));

        // run
        studentService.addStudent(studentDto);

        // verify
        verify(studentRepository).save(student);
    }


    @Test
    void getStudents() {
        // data
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setFirstName("First");
        student.setDateOfBirth(getDate("1999-01-01"));


        Student secondStudent = new Student();
        secondStudent.setFirstName("Second");
        secondStudent.setDateOfBirth(getDate("2000-01-01"));

        studentList.add(secondStudent);
        studentList.add(student);

        when(studentRepository.findAll()).thenReturn(studentList);

        // run
        List<StudentDto> resultList = studentService.getStudents();

        // verify
        assertEquals("First", resultList.get(0).getFirstName());
        assertEquals("Second", resultList.get(1).getFirstName());
        assertEquals("1999-01-01", resultList.get(0).getDateOfBirth());
        assertEquals("2000-01-01", resultList.get(1).getDateOfBirth());
    }

    @Test
    void studentsSeeCourses() {
        // data
        Student student = new Student();
        student.setId(1);
        student.setDateOfBirth(getDate("2000-01-01"));

        List<Course> courseList = new ArrayList<>();
        Course course = new Course();
        course.setTitle("First");
        course.setStartingDay(getDate("2000-01-01"));

        Course secondCourse = new Course();
        secondCourse.setTitle("Second");
        secondCourse.setStartingDay(getDate("2000-01-01"));

        courseList.add(secondCourse);
        courseList.add(course);

        student.setCourses(courseList);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        // run
        List<CourseDto> resultList = studentService.studentsSeeCourses(1);

        // verify
        assertEquals("First", resultList.get(0).getTitle());
        assertEquals("Second", resultList.get(1).getTitle());
        assertEquals("2000-01-01", resultList.get(0).getStartingDay());
        assertEquals("2000-01-01", resultList.get(1).getStartingDay());

    }

    @Test
    void studentsAddCourses() {
        // data
        Student student = new Student();
        student.setId(1);
        student.setDateOfBirth(getDate("2000-01-01"));

        List<Course> courseList = new ArrayList<>();

        Course course = new Course();
        course.setTitle("First");
        course.setStartingDay(getDate("2000-01-01"));

        Course secondCourse = new Course();
        secondCourse.setTitle("Second");
        secondCourse.setStartingDay(getDate("2000-01-01"));

        Course thirdCourse = new Course();
        thirdCourse.setTitle("Third");
        thirdCourse.setStartingDay(getDate("2000-01-01"));

        courseList.add(secondCourse);
        courseList.add(course);
        courseList.add(thirdCourse);

        List<Course> studentCourseList = new ArrayList<>();
        studentCourseList.add(secondCourse);
        studentCourseList.add(course);
        student.setCourses(studentCourseList);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        course.setStudentsEnrolled(studentList);
        secondCourse.setStudentsEnrolled(studentList);
        thirdCourse.setStudentsEnrolled(new ArrayList<>());

        when(courseRepository.findAll()).thenReturn(courseList);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        // run
        List<CourseDto> resultList = studentService.studentsAddCourses(1);

        // verify
        verify(studentRepository).findById(1);
        assertEquals(1, resultList.size());
        assertEquals("Third", resultList.get(0).getTitle());
        assertEquals("2000-01-01", resultList.get(0).getStartingDay());
    }

    @Test
    void deleteCourseForStudent() {
        // data
        Student student = new Student();
        student.setId(1);
        student.setDateOfBirth(getDate("2000-01-01"));

        List<Course> studentCourseList = new ArrayList<>();
        Course course = new Course();
        course.setId(10);
        course.setStartingDay(getDate("2000-01-01"));

        student.setCourses(studentCourseList);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        // run
        studentService.deleteCourseForStudent(course.getId(), student.getId());

        // verify
        assertFalse(student.getCourses().contains(course));
        verify(studentRepository).save(student);
    }

    @Test
    void addCourseToStudent() {
        // data
        Student student = new Student();
        student.setId(1);
        student.setDateOfBirth(getDate("2000-01-01"));
        student.setCourses(new ArrayList<>());

        Course course = new Course();
        course.setId(10);
        course.setTitle("First");
        course.setStartingDay(getDate("2000-01-01"));

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(10)).thenReturn(Optional.of(course));

        // run
        studentService.addCourseToStudent(student.getId(), course.getId());

        // verify
        assertTrue(student.getCourses().contains(course));
        verify(studentRepository).save(student);
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