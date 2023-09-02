package com.example.university.course.service;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.course.repository.CourseRepository;
import com.example.university.student.controller.StudentDto;
import com.example.university.student.entity.Student;
import com.example.university.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
//    private final Course2StudentRepository course2StudentRepository;

    public void addCourse(CourseDto courseDto) {
        Course course = setCourse(courseDto);
        courseRepository.save(course);
    }

    public List<CourseDto> getCourses() {
        List<Course> courseList = courseRepository.findAll();
        List<CourseDto> courseDtoList = new ArrayList<>();

        for (Course course : courseList) {
            CourseDto courseDto = setCourseDto(course);
            courseDtoList.add(courseDto);
        }

        return courseDtoList;
    }

    public List<StudentDto> getStudentsInCourse(Integer courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        List<Student> studentList = course.get().getStudentsEnrolled();
        List<StudentDto> studentDtoList = new ArrayList<>();
        for (Student student : studentList) {
            studentDtoList.add(setStudentDto(student));
        }
        return studentDtoList;
    }

    private CourseDto setCourseDto(Course course) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setStartingDay(dateFormat.format(course.getStartingDay()));

        return courseDto;
    }

    private StudentDto setStudentDto(Student student) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setDateOfBirth(dateFormat.format(student.getDateOfBirth()));

        return studentDto;
    }

    private Course setCourse(CourseDto courseDto) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(courseDto.getStartingDay());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Course course = new Course();
        course.setId(courseDto.getId());
        course.setTitle(courseDto.getTitle());
        course.setStartingDay(date);
        return course;
    }
}
