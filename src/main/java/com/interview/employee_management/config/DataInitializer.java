package com.interview.employee_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.interview.employee_management.entity.Department;
import com.interview.employee_management.entity.Employee;
import com.interview.employee_management.repository.DepartmentRepository;
import com.interview.employee_management.repository.EmployeeRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (departmentRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        // Create HR Department
        Department hrDept = new Department("dept01", "Human Resources", "Building A");
        departmentRepository.save(hrDept);
        
        Employee alice = new Employee("emp001", "Alice Smith", "alice.smith@example.com", "Recruiter", 60000.0);
        alice.setDepartment(hrDept);
        employeeRepository.save(alice);
        
        Employee charlie = new Employee("emp003", "Charlie Brown", "charlie.brown@example.com", "HR Assistant", 40000.0);
        charlie.setDepartment(hrDept);
        employeeRepository.save(charlie);
        
        // Create Engineering Department
        Department engDept = new Department("dept02", "Engineering", "Building B");
        departmentRepository.save(engDept);
        
        Employee bob = new Employee("emp002", "Bob Johnson", "bob.johnson@example.com", "Software Engineer", 80000.0);
        bob.setDepartment(engDept);
        employeeRepository.save(bob);
        
        Employee diana = new Employee("emp004", "Diana Prince", "diana.prince@example.com", "System Architect", 90000.0);
        diana.setDepartment(engDept);
        employeeRepository.save(diana);
        
        System.out.println("Sample data initialized successfully!");
    }
}