package com.interview.employee_management.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interview.employee_management.dto.DepartmentDTO;
import com.interview.employee_management.dto.EmployeeDTO;
import com.interview.employee_management.entity.Department;
import com.interview.employee_management.entity.Employee;
import com.interview.employee_management.repository.DepartmentRepository;

@Service
@Transactional
public class DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartmentsWithEmployees() {
        List<Department> departments = departmentRepository.findAllWithEmployees();
        return departments.stream()
                .map(this::convertToDTOWithEmployees)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .map(this::convertToDTOWithEmployees);
    }
    
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        
        // Generate ID if not provided
        if (department.getId() == null || department.getId().trim().isEmpty()) {
            department.setId(generateNextDepartmentId());
        }
        
        // Check if department ID already exists
        if (departmentRepository.existsById(department.getId())) {
            throw new RuntimeException("Department ID already exists: " + department.getId());
        }
        
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }
    
    @Transactional
    public DepartmentDTO updateDepartment(String id, DepartmentDTO departmentDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        existingDepartment.setName(departmentDTO.getName());
        existingDepartment.setLocation(departmentDTO.getLocation());
        
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return convertToDTO(updatedDepartment);
    }
    
    @Transactional
    public void deleteDepartment(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        if (!department.getEmployees().isEmpty()) {
            throw new RuntimeException("Cannot delete department with employees. Please reassign or remove employees first.");
        }
        
        departmentRepository.deleteById(id);
    }
    
    // NEW: Generate next department ID
    @Transactional(readOnly = true)
    public String generateNextDepartmentId() {
        List<Department> departments = departmentRepository.findAll();
        int maxNum = 0;
        for (Department dept : departments) {
            String id = dept.getId();
            if (id.startsWith("dept")) {
                try {
                    int num = Integer.parseInt(id.substring(4));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException e) {
                    // Ignore non-numeric IDs
                }
            }
        }
        return String.format("dept%02d", maxNum + 1);
    }
    
    // Demonstrating Map and List usage
    @Transactional(readOnly = true)
    public Map<String, Integer> getDepartmentEmployeeCounts() {
        List<Department> departments = departmentRepository.findAllWithEmployees();
        Map<String, Integer> employeeCounts = new HashMap<>();
        
        for (Department dept : departments) {
            employeeCounts.put(dept.getName(), dept.getEmployees().size());
        }
        
        return employeeCounts;
    }
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDepartmentSummary() {
        List<Department> departments = departmentRepository.findAllWithEmployees();
        List<Map<String, Object>> summary = new ArrayList<>();
        
        for (Department dept : departments) {
            Map<String, Object> deptSummary = new HashMap<>();
            deptSummary.put("departmentId", dept.getId());
            deptSummary.put("departmentName", dept.getName());
            deptSummary.put("location", dept.getLocation());
            deptSummary.put("employeeCount", dept.getEmployees().size());
            
            if (!dept.getEmployees().isEmpty()) {
                double avgSalary = dept.getEmployees().stream()
                        .mapToDouble(Employee::getSalary)
                        .average()
                        .orElse(0.0);
                deptSummary.put("averageSalary", avgSalary);
            } else {
                deptSummary.put("averageSalary", 0.0);
            }
            
            summary.add(deptSummary);
        }
        
        return summary;
    }
    
    // Conversion methods
    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());
        return dto;
    }
    
    private DepartmentDTO convertToDTOWithEmployees(Department department) {
        DepartmentDTO dto = convertToDTO(department);
        if (department.getEmployees() != null) {
            List<EmployeeDTO> employeeDTOs = department.getEmployees().stream()
                    .map(this::convertEmployeeToDTO)
                    .collect(Collectors.toList());
            dto.setEmployees(employeeDTOs);
        }
        return dto;
    }
    
    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        return dto;
    }
    
    private Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        department.setLocation(dto.getLocation());
        return department;
    }
}