package com.example.university.professor.controller;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.professor.entity.Professor;
import com.example.university.professor.service.ProfessorService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ProfessorControllerTest {
    private final ProfessorService professorService = mock(ProfessorService.class);
    private final ProfessorController professorController = new ProfessorController(professorService);

    @Test
    void getProfessors() {
        // data
        Model model = mock(Model.class);
        List<ProfessorDto> professorDtoList = new ArrayList<>();
        when(professorService.getProfessors()).thenReturn(professorDtoList);
        // run
        String result = professorController.getProfessors(model);
        // verify
        verify(model).addAttribute("professorList", professorDtoList);
        assertEquals("professor/professorList", result);

    }

    @Test
    void getProfessorForm() {
        // data
        // run
        ModelAndView result = professorController.getProfessorForm();
        // verify
        assertEquals("professor/addProfessor", result.getViewName());
        Professor professor = (Professor) result.getModel().get("professor");
        assertNull(professor.getId());
        assertNull(professor.getName());
        assertNull(professor.getCourses());
    }

    @Test
    void addProfessor() {
        // data
        ProfessorDto professorDto = new ProfessorDto();
        // run
        String result = professorController.addProfessor(professorDto);
        // verify
        verify(professorService).addProfessor(professorDto);
        assertEquals("redirect:/professors", result);

    }

    @Test
    void professorsAddCourses() {
        // data
        Professor professor = new Professor();
        professor.setId(1);

        Model model = mock(Model.class);

        List<CourseDto> courseDtoList = new ArrayList<>();
        when(professorService.professorAddsCourses(1)).thenReturn(courseDtoList);

        // run
        String result = professorController.professorsAddCourses(professor.getId(), model);

        // verify
        verify(model).addAttribute("courseDtoList", courseDtoList);
        verify(model).addAttribute("professorId", 1);
        assertEquals("professor/professorsAddCourses", result);
    }

    @Test
    void addCourseToStudent() {
        // data
        Professor professor = new Professor();
        professor.setId(1);

        Course course = new Course();
        course.setId(10);

        // run
        String result = professorController.addCourseToStudent(1, 10);

        // verify
        verify(professorService).addCourseToProfessor(1, 10);
        assertEquals("redirect:/professorsAddCourses?id=1", result);
    }

    @Test
    void getCoursesOfProfessor() {
        // data
        Model model = mock(Model.class);
        List<CourseDto> courseDtoList = new ArrayList<>();
        Professor professor = new Professor();
        professor.setId(1);

        when(professorService.getCoursesOfProfessor(professor.getId())).thenReturn(courseDtoList);

        // run
        String result = professorController.getCoursesOfProfessor(professor.getId(), model);

        // verify
        verify(model).addAttribute("coursesOfProfessor", courseDtoList);
        verify(model).addAttribute("professorId", 1);
        assertEquals("professor/seeProfessorCourses", result);
    }

    @Test
    void deleteCourseForProfessor() {
        // data
        Course course = new Course();
        course.setId(10);

        Professor professor = new Professor();
        professor.setId(1);
        professor.setCourses(Collections.singletonList(course));

        // run
        String result = professorController.deleteCourseForProfessor(professor.getId(), course.getId());

        // verify
        verify(professorService).deleteCourseForProfessor(10);
        assertEquals("redirect:/seeProfessorCourses?id=1", result);
    }
}