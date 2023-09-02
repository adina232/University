package com.example.university.professor.controller;

import com.example.university.professor.entity.Professor;
import com.example.university.professor.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfessorController {
    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/professors")
    public String getProfessors(Model model) {
        model.addAttribute("professorList", professorService.getProfessors());
        return "professor/professorList";
    }

    @GetMapping("/addProfessor")
    public ModelAndView getProfessorForm() {
        ModelAndView modelAndView = new ModelAndView("professor/addProfessor");
        modelAndView.addObject("professor", new Professor());
        return modelAndView;
    }

    @PostMapping("/addProfessor")
    public String addProfessor(@ModelAttribute ProfessorDto professorDto) {
        professorService.addProfessor(professorDto);
        return "redirect:/professors";
    }

    @GetMapping("/professorsAddCourses")
    public String professorsAddCourses(@RequestParam Integer id, Model model) {
        model.addAttribute("courseDtoList", professorService.professorAddsCourses(id));
        model.addAttribute("professorId", id);
        return "professor/professorsAddCourses";
    }

    @GetMapping("/addCourseToProfessor")
    public String addCourseToStudent(@RequestParam Integer professorId, @RequestParam Integer courseId) {
        professorService.addCourseToProfessor(professorId, courseId);
        return "redirect:/professorsAddCourses?id=" + professorId;
    }


    @GetMapping("/seeProfessorCourses")
    public String getCoursesOfProfessor(@RequestParam Integer id, Model model) {
        model.addAttribute("coursesOfProfessor", professorService.getCoursesOfProfessor(id));
        model.addAttribute("professorId", id);
        return "professor/seeProfessorCourses";
    }

    @GetMapping("/deleteCourseForProfessor")
    public String deleteCourseForProfessor(@RequestParam Integer professorId, @RequestParam Integer courseId) {
        professorService.deleteCourseForProfessor(courseId);
        return "redirect:/seeProfessorCourses?id=" + professorId;
    }

}
