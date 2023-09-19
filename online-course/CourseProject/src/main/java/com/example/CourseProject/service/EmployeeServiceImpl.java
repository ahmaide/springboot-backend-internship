package com.example.CourseProject.service;

import com.example.CourseProject.dao.EmployeeRepository;
import com.example.CourseProject.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;

    }
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int empId) {
        Optional<Employee> result = employeeRepository.findById(empId);
        Employee temp = null;
        if (result.isPresent())
            temp = result.get();
        else{
            throw new RuntimeException("Did not find employee id - " + empId);
        }

        return temp;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteById(int empId){
        employeeRepository.deleteById(empId);
    }
}
