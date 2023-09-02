package com.example.university.professor.controller;

import com.example.university.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDto {
    private Integer id;
    private String name;
    private List<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfessorDto)) return false;
        ProfessorDto that = (ProfessorDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(courses, that.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courses);
    }
}
