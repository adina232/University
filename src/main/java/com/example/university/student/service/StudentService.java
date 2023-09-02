package com.example.university.student.service;

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
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    public void addStudent(StudentDto studentDto) {
        Student student = setStudent(studentDto);

        studentRepository.save(student);
    }

    public List<StudentDto> getStudents() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> studentDtoList = new ArrayList<>();

        for (Student student : studentList) {
            studentDtoList.add(setStudentDto(student));
        }

        Collections.sort(studentDtoList, new Comparator<StudentDto>() {
            @Override
            public int compare(StudentDto s1, StudentDto s2) {
                return s1.getFirstName().compareTo(s2.getFirstName());
            }
        });
        return studentDtoList;
    }

    public List<CourseDto> studentsSeeCourses(Integer id) {
        Optional<Student> student = studentRepository.findById(id);
        List<Course> courseList = student.get().getCourses();
        List<CourseDto> courseListDto = new ArrayList<>();

        for (Course course : courseList) {
            courseListDto.add(setCourseDto(course));
        }


        Collections.sort(courseListDto, new Comparator<CourseDto>() {
            @Override
            public int compare(CourseDto c1, CourseDto c2) {
                return c1.getTitle().compareTo(c2.getTitle());
            }
        });

        return courseListDto;
    }

    public List<CourseDto> studentsAddCourses(Integer id) {
        List<Course> courseList = courseRepository.findAll();
        List<CourseDto> courseListDto = new ArrayList<>();

        Optional<Student> student = studentRepository.findById(id);
        for (Course course : courseList) {
            if (!course.getStudentsEnrolled().contains(student.get())) {
                courseListDto.add(setCourseDto(course));
            }
        }

        Collections.sort(courseListDto, new Comparator<CourseDto>() {
            @Override
            public int compare(CourseDto c1, CourseDto c2) {
                return c1.getTitle().compareTo(c2.getTitle());
            }
        });

        return courseListDto;
    }

    public void deleteCourseForStudent(Integer courseId, Integer studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        List<Course> courseList = student.get().getCourses();

        courseList.removeIf(course -> course.getId().equals(courseId));
        studentRepository.save(student.get());
    }

    public void addCourseToStudent(Integer studentId, Integer courseId) {
        Optional<Student> student = studentRepository.findById(studentId);

        Optional<Course> course = courseRepository.findById(courseId);
        student.get().getCourses().add(course.get());

        studentRepository.save(student.get());
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

    private CourseDto setCourseDto(Course course) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setStartingDay(dateFormat.format(course.getStartingDay()));

        return courseDto;
    }

    private Student setStudent(StudentDto studentDto) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(studentDto.getDateOfBirth());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Student student = new Student();
        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setDateOfBirth(date);

        return student;
    }
}
