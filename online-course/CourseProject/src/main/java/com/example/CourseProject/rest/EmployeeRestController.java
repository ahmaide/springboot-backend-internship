package com.example.CourseProject.rest;

import com.example.CourseProject.dao.EmployeeDAO;
import com.example.CourseProject.entity.Employee;
import com.example.CourseProject.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService){
        this.employeeService= employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> findAll(){
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId){
        Employee employee = employeeService.findById(employeeId);

        if(employee == null){
            throw new RuntimeException("Employee id is not found - " + employeeId);
        }
        return employee;
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee1){
        employee1.setId(0);
        Employee emp2 = employeeService.save(employee1);
        return emp2;
    }

    @PutMapping("/employees")
    public Employee  updateEmployee(@RequestBody Employee employee){
        Employee emp2 = employeeService.save(employee);
        return emp2;
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId){
        Employee employee = employeeService.findById(employeeId);
        if(employee == null){
            throw new RuntimeException("Employee id is not found - " + employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Deleted employee id - " + employeeId;
    }
}
