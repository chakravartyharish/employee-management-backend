package com.interview.employee_management.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class DepartmentDTO {
    
    private String id;
    
    @NotBlank(message = "Department name is required")
    private String name;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private List<EmployeeDTO> employees;
    
    // Constructors
    public DepartmentDTO() {}
    
    public DepartmentDTO(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public List<EmployeeDTO> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }
}