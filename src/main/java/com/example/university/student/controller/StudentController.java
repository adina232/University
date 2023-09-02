package com.example.university.student.controller;

import com.example.university.student.entity.Student;
import com.example.university.student.service.StudentService;
import lombok.var;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/addStudent")
    public ModelAndView showStudentForm() {
        var modelAndView = new ModelAndView();
        modelAndView.addObject("student", new Student());
        modelAndView.setViewName("students/addStudent");
        return modelAndView;
    }

    @PostMapping("/addStudent")
    public String addStudent(@ModelAttribute StudentDto studentDto) {
        studentService.addStudent(studentDto);
        return "redirect:/studentList";
    }

    @GetMapping("/studentList")
    public String studentList(Model model) {
        model.addAttribute("studentList", studentService.getStudents());
        return "students/studentList";
    }

    @GetMapping("/studentsAddCourses")
    public String studentsAddCourses(@RequestParam Integer id, Model model) {
        model.addAttribute("courseListDto", studentService.studentsAddCourses(id));
        model.addAttribute("studentId", id);
        return "students/studentsAddCourses";
    }

    @GetMapping("/studentsSeeCourses")
    public String studentsSeeCourses(@RequestParam Integer id, Model model) {
        model.addAttribute("courses", studentService.studentsSeeCourses(id));
        model.addAttribute("studentId", id);
        return "students/studentsSeeCourses";
    }

    @GetMapping("/deleteCourseForStudent")
    public String deleteCourseForStudent(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        studentService.deleteCourseForStudent(studentId, courseId);
        return "redirect:/studentsSeeCourses?id=" + studentId;
    }

    @GetMapping("/addCourseToStudent")
    public String addCourseToStudent(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        studentService.addCourseToStudent(studentId, courseId);
        return "redirect:/studentsAddCourses?id=" + studentId;
    }


}
