package com.example.CourseProject.dao;

import com.example.CourseProject.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll();

    Employee findById(int empId);

    Employee save (Employee employee);

    void deleteById(int empId);

}
