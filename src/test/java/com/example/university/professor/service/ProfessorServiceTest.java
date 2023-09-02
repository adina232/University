package com.example.university.professor.service;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.course.repository.CourseRepository;
import com.example.university.professor.controller.ProfessorDto;
import com.example.university.professor.entity.Professor;
import com.example.university.professor.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ProfessorServiceTest {
    private final ProfessorRepository professorRepository = mock(ProfessorRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final ProfessorService professorService = new ProfessorService(professorRepository, courseRepository);

    @Test
    void getProfessors() {
        // data
        Professor professor = new Professor();
        professor.setId(1);
        when(professorRepository.findAll()).thenReturn(Collections.singletonList(professor));

        // run
        List<ProfessorDto> result = professorService.getProfessors();

        // verify
        assertEquals(1, result.get(0).getId());
        assertNull(result.get(0).getName());
        assertNull(result.get(0).getCourses());
    }

    @Test
    void addProfessor() {
        // data
        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setId(1);
        professorDto.setName("Test");

        Professor professor = new Professor();
        professor.setId(1);
        professor.setName("Test");

        // run
        professorService.addProfessor(professorDto);

        // verify
        verify(professorRepository).save(professor);
    }

    @Test
    void getCoursesOfProfessor() {
        // data
        Professor professor = new Professor();
        professor.setId(1);
        List<Course> courseList = new ArrayList<>();
        Course course = new Course();
        course.setStartingDay(getDate("2000-01-01"));
        courseList.add(course);
        professor.setCourses(courseList);

        when(professorRepository.findById(1)).thenReturn(Optional.of(professor));

        // run
        List<CourseDto> resultList = professorService.getCoursesOfProfessor(professor.getId());

        // verify
        assertEquals(1, resultList.size());
        assertNull(resultList.get(0).getId());
        assertNull(resultList.get(0).getTitle());
        assertNull(resultList.get(0).getStudentsEnrolled());
        assertEquals("2000-01-01", resultList.get(0).getStartingDay());
    }

    @Test
    void professorAddsCourses() {
        // data
        Course course = new Course();
        course.setId(1);
        course.setTitle("first");
        course.setStartingDay(getDate("2022-01-01"));

        Course secondCourse = new Course();
        secondCourse.setId(2);
        secondCourse.setTitle("second");
        secondCourse.setStartingDay(getDate("2022-01-01"));

        Course thirdCourse = new Course();
        thirdCourse.setId(3);
        thirdCourse.setTitle("third");
        thirdCourse.setStartingDay(getDate("2022-01-01"));

        List<Course> courseList = new ArrayList<>();
        courseList.add(thirdCourse);
        courseList.add(secondCourse);
        courseList.add(course);

        when(courseRepository.findAll()).thenReturn(courseList);

        Professor professor = new Professor();
        professor.setId(10);
        List<Course> professorCourseList = new ArrayList<>();
        professorCourseList.add(secondCourse);
        professor.setCourses(professorCourseList);

        when(professorRepository.findById(professor.getId())).thenReturn(Optional.of(professor));

        // run
        List<CourseDto> resultList = professorService.professorAddsCourses(professor.getId());

        // verify
        assertEquals(2, resultList.size());
        assertEquals("first", resultList.get(0).getTitle());
        assertEquals(1, resultList.get(0).getId());
        assertEquals("third", resultList.get(1).getTitle());
        assertEquals(3, resultList.get(1).getId());

    }

    @Test
    void addCourseToProfessor() {
        // data
        Professor professor = new Professor();
        professor.setId(10);

        Course course = new Course();
        course.setId(1);
        course.setStartingDay(getDate("2000-01-01"));

        when(professorRepository.findById(professor.getId())).thenReturn(Optional.of(professor));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        // run
        professorService.addCourseToProfessor(professor.getId(), course.getId());

        // verify
        verify(courseRepository).save(course);

    }

    @Test
    void deleteCourseForProfessor() {
        // data
        Course course = new Course();
        course.setId(1);
        course.setStartingDay(getDate("2000-01-01"));

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        // run
        professorService.deleteCourseForProfessor(course.getId());

        // verify
        verify(courseRepository).save(course);

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