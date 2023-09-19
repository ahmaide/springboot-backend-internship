package com.example.CourseProject.service;

import com.example.CourseProject.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(int empId);

    Employee save (Employee employee);

    void deleteById(int empId);
}
