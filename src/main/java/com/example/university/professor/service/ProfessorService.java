package com.example.university.professor.service;

import com.example.university.course.controller.CourseDto;
import com.example.university.course.entity.Course;
import com.example.university.course.repository.CourseRepository;
import com.example.university.professor.controller.ProfessorDto;
import com.example.university.professor.entity.Professor;
import com.example.university.professor.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    public ProfessorService(ProfessorRepository professorRepository, CourseRepository courseRepository) {
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
    }


    public List<ProfessorDto> getProfessors() {
        List<Professor> professorList = professorRepository.findAll();
        List<ProfessorDto> professorDtoList = new ArrayList<>();
        for (Professor professor : professorList) {
            professorDtoList.add(setProfessorDto(professor));
        }
        return professorDtoList;
    }

    public void addProfessor(ProfessorDto professorDto) {
        Professor professor = setProfessor(professorDto);
        professorRepository.save(professor);
    }

    public List<CourseDto> getCoursesOfProfessor(Integer id) {
        Optional<Professor> professor = professorRepository.findById(id);
        List<CourseDto> courseDtoList = new ArrayList<>();
        List<Course> courseList = professor.get().getCourses();
        for (Course course : courseList) {
            courseDtoList.add(setCourseDto(course));
        }
        return courseDtoList;
    }

    public List<CourseDto> professorAddsCourses(Integer professorId) {
        List<Course> courseList = courseRepository.findAll();
        List<CourseDto> courseDtoList = new ArrayList<>();

        Collections.sort(courseList, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                return c1.getTitle().compareTo(c2.getTitle());
            }
        });


        Optional<Professor> professor = professorRepository.findById(professorId);
        for (Course course : courseList) {
            if (!professor.get().getCourses().contains(course)) {
                courseDtoList.add(setCourseDto(course));
            }
        }

        return courseDtoList;
    }

    public void addCourseToProfessor(Integer professorId, Integer courseId) {
        Optional<Professor> professor = professorRepository.findById(professorId);
        Optional<Course> course = courseRepository.findById(courseId);
        course.get().setProfessor(professor.get());

        courseRepository.save(course.get());
    }

    public void deleteCourseForProfessor(Integer courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        course.get().setProfessor(null);

        courseRepository.save(course.get());
    }


    private ProfessorDto setProfessorDto(Professor professor) {
        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setId(professor.getId());
        professorDto.setName(professor.getName());

        return professorDto;
    }

    private Professor setProfessor(ProfessorDto professorDto) {
        Professor professor = new Professor();
        professor.setId(professorDto.getId());
        professor.setName(professorDto.getName());

        return professor;
    }

    private CourseDto setCourseDto(Course course) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());
        courseDto.setStartingDay(dateFormat.format(course.getStartingDay()));

        return courseDto;
    }
}
