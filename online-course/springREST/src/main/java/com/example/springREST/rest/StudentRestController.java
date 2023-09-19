package com.example.springREST.rest;

import com.example.springREST.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> students;

    @PostConstruct
    public void loadData(){
        students = new ArrayList<>();

        students.add(new Student("Hanna", "Bullata"));
        students.add(new Student("Khader", "Mohammad"));
        students.add(new Student("Wasel", "Ghanem"));
        students.add(new Student("Ahmad", "Alsadeh"));
        students.add(new Student("Ayman", "Hroub"));
    }

    // endpoint
    @GetMapping("/students")
    public List<Student> getStudents(){

        return students;
    }
    /*
   @GetMapping("/students")
    public List<Student> getStudents(){

        List<Student> students = new ArrayList<>();

        students.add(new Student("Hanna", "Bullata"));
        students.add(new Student("Khader", "Mohammad"));
        students.add(new Student("Wasel", "Ghanem"));
        students.add(new Student("Ahmad", "Alsadeh"));
        students.add(new Student("Ayman", "Hroub"));
        return students;
    }*/

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable int studentId){

        if(studentId >= students.size() || studentId<0){
            throw new StudentNotFound("Student is not found - " + studentId);
        }
        return students.get(studentId);
    }

    /*@ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFound exc){

        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(Exception exc){
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }*/

}
