package com.example.university.course.controller;

import com.example.university.professor.controller.ProfessorDto;
import com.example.university.student.controller.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Integer id;
    private String title;
    private ProfessorDto professor;
    private String startingDay;
    //    private double grade;
    private List<StudentDto> studentsEnrolled;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseDto)) return false;
        CourseDto courseDto = (CourseDto) o;
        return Objects.equals(id, courseDto.id) && Objects.equals(title, courseDto.title) && Objects.equals(startingDay, courseDto.startingDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, startingDay);
    }
}
