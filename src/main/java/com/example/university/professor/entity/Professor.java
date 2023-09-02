package com.example.university.professor.entity;

import com.example.university.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "professors")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "courses")
    @OneToMany(
            mappedBy = "professor",
            orphanRemoval = true
    )
    private List<Course> courses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        Professor professor = (Professor) o;
        return Objects.equals(id, professor.id) && Objects.equals(name, professor.name) && Objects.equals(courses, professor.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courses);
    }
}
