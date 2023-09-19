package com.example.CourseProject.dao;

import com.example.CourseProject.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO{

    private EntityManager entityManager;

    @Autowired
    public EmployeeDAOJpaImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public List<Employee> findAll() {

        TypedQuery<Employee> theQuery = entityManager.createQuery("from Employee", Employee.class);

        List<Employee> employees = theQuery.getResultList();
        return employees;
    }

    @Override
    public Employee findById(int empId) {
        Employee emp = entityManager.find(Employee.class, empId);
        return emp;
    }

    @Override
    public Employee save(Employee employee) {
        Employee emp = entityManager.merge(employee);
        return emp;
    }

    @Override
    public void deleteById(int empId) {
        Employee emp = entityManager.find(Employee.class, empId);
        entityManager.remove(emp);
    }
}
