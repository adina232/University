package com.example.university.course.controller;

import com.example.university.course.entity.Course;
import com.example.university.course.service.CourseService;
import com.example.university.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final StudentService studentService;


    @GetMapping("/addCourse")
    public ModelAndView showCourseForm() {
        var modelAndView = new ModelAndView("/course/addCourse");
        modelAndView.addObject("course", new Course());
        return modelAndView;
    }

    @PostMapping("/addCourse")
    public String addCourse(@ModelAttribute CourseDto courseDto) {
        courseService.addCourse(courseDto);
        return "redirect:/courseList";
    }

    @GetMapping("/courseList")
    public String getCourses(Model model) {
        model.addAttribute("courseList", courseService.getCourses());
        return "course/courseList";
    }

    @GetMapping("/seeStudents")
    public String seeStudents(@RequestParam Integer courseId, Model model) {
        model.addAttribute("studentsInCourse", courseService.getStudentsInCourse(courseId));
        model.addAttribute("courseId", courseId);
        return "course/seeStudents";
    }

    @GetMapping("/deleteStudentFromCourseList")
    public String deleteStudentFromCourseList(@RequestParam Integer courseId,
                                              @RequestParam Integer studentId) {
        studentService.deleteCourseForStudent(studentId, courseId);
        return "redirect:/seeStudents?courseId=" + courseId;
    }
}
