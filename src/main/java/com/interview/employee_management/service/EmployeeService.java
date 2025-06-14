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

import com.interview.employee_management.dto.EmployeeDTO;
import com.interview.employee_management.entity.Department;
import com.interview.employee_management.entity.Employee;
import com.interview.employee_management.repository.DepartmentRepository;
import com.interview.employee_management.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Transactional
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO, String departmentId) {
        // Check if department exists
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
        
        // Check if employee ID already exists
        if (employeeRepository.existsById(employeeDTO.getId())) {
            throw new RuntimeException("Employee ID already exists: " + employeeDTO.getId());
        }
        
        // Check if employee email already exists
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Employee with email already exists: " + employeeDTO.getEmail());
        }
        
        Employee employee = convertToEntity(employeeDTO);
        employee.setDepartment(department);
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }
    
    @Transactional
    public EmployeeDTO updateEmployee(String id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail()) && 
            employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Employee with email already exists: " + employeeDTO.getEmail());
        }
        
        // Update fields
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setPosition(employeeDTO.getPosition());
        existingEmployee.setSalary(employeeDTO.getSalary());
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToDTO(updatedEmployee);
    }
    
    @Transactional
    public void deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    // NEW: Method to check if employee ID exists
    @Transactional(readOnly = true)
    public boolean employeeIdExists(String id) {
        return employeeRepository.existsById(id);
    }
    
    // NEW: Method to get next available employee ID
    @Transactional(readOnly = true)
    public String getNextEmployeeId() {
        List<Employee> employees = employeeRepository.findAll();
        int maxNum = 0;
        for (Employee emp : employees) {
            String id = emp.getId();
            if (id.startsWith("emp")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    maxNum = Math.max(maxNum, num);
                } catch (NumberFormatException e) {
                    // Ignore non-numeric IDs
                }
            }
        }
        return String.format("emp%03d", maxNum + 1);
    }
    
    // NEW: Method to validate employee data
    @Transactional(readOnly = true)
    public void validateEmployeeData(EmployeeDTO employeeDTO, boolean isUpdate, String currentId) {
        if (employeeDTO.getName() == null || employeeDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("Employee name is required");
        }
        
        if (employeeDTO.getEmail() == null || employeeDTO.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Employee email is required");
        }
        
        if (employeeDTO.getPosition() == null || employeeDTO.getPosition().trim().isEmpty()) {
            throw new RuntimeException("Employee position is required");
        }
        
        if (employeeDTO.getSalary() == null || employeeDTO.getSalary() <= 0) {
            throw new RuntimeException("Employee salary must be greater than 0");
        }
        
        // Email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!employeeDTO.getEmail().matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
        }
        
        // For updates, don't check email uniqueness if it's the same employee
        if (isUpdate && currentId != null) {
            Optional<Employee> currentEmployee = employeeRepository.findById(currentId);
            if (currentEmployee.isPresent() && 
                !currentEmployee.get().getEmail().equals(employeeDTO.getEmail()) &&
                employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                throw new RuntimeException("Employee with email already exists: " + employeeDTO.getEmail());
            }
        } else if (!isUpdate && employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Employee with email already exists: " + employeeDTO.getEmail());
        }
    }
    
    // Demonstrating Map and List usage
    @Transactional(readOnly = true)
    public Map<String, List<EmployeeDTO>> getEmployeesGroupedByDepartment() {
        List<Employee> allEmployees = employeeRepository.findAll();
        
        Map<String, List<EmployeeDTO>> employeesByDepartment = new HashMap<>();
        
        for (Employee employee : allEmployees) {
            String deptName = employee.getDepartment().getName();
            employeesByDepartment.computeIfAbsent(deptName, k -> new ArrayList<>())
                    .add(convertToDTO(employee));
        }
        
        return employeesByDepartment;
    }
    
    // FIXED METHOD - Corrected the iteration logic
    @Transactional(readOnly = true)
    public Map<String, Double> getDepartmentSalaryStatistics() {
        List<Employee> allEmployees = employeeRepository.findAll();
        Map<String, Double> salaryStats = new HashMap<>();
        
        Map<String, List<Double>> salariesByDept = allEmployees.stream()
                .collect(Collectors.groupingBy(
                        emp -> emp.getDepartment().getName(),
                        Collectors.mapping(Employee::getSalary, Collectors.toList())
                ));
        
        // FIX: Iterate over salariesByDept, not salaryStats
        for (Map.Entry<String, List<Double>> entry : salariesByDept.entrySet()) {
            List<Double> salaries = entry.getValue();
            double avgSalary = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            salaryStats.put(entry.getKey(), avgSalary);
        }
        
        return salaryStats;
    }
    
    // Alternative more concise version of the above method
    @Transactional(readOnly = true)
    public Map<String, Double> getDepartmentSalaryStatisticsStreamVersion() {
        List<Employee> allEmployees = employeeRepository.findAll();
        
        return allEmployees.stream()
                .collect(Collectors.groupingBy(
                        emp -> emp.getDepartment().getName(),
                        Collectors.averagingDouble(Employee::getSalary)
                ));
    }
    
    // NEW: Get employee statistics
    @Transactional(readOnly = true)
    public Map<String, Object> getEmployeeStatistics() {
        List<Employee> allEmployees = employeeRepository.findAll();
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalEmployees", allEmployees.size());
        
        if (!allEmployees.isEmpty()) {
            double avgSalary = allEmployees.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0.0);
            stats.put("averageSalary", avgSalary);
            
            double maxSalary = allEmployees.stream()
                    .mapToDouble(Employee::getSalary)
                    .max()
                    .orElse(0.0);
            stats.put("maxSalary", maxSalary);
            
            double minSalary = allEmployees.stream()
                    .mapToDouble(Employee::getSalary)
                    .min()
                    .orElse(0.0);
            stats.put("minSalary", minSalary);
        } else {
            stats.put("averageSalary", 0.0);
            stats.put("maxSalary", 0.0);
            stats.put("minSalary", 0.0);
        }
        
        return stats;
    }
    
    // NEW: Search employees by various criteria
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployees(String searchTerm) {
        List<Employee> allEmployees = employeeRepository.findAll();
        
        return allEmployees.stream()
                .filter(emp -> 
                    emp.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    emp.getEmail().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    emp.getPosition().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    emp.getId().toLowerCase().contains(searchTerm.toLowerCase())
                )
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // NEW: Get employees by salary range
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesBySalaryRange(Double minSalary, Double maxSalary) {
        List<Employee> allEmployees = employeeRepository.findAll();
        
        return allEmployees.stream()
                .filter(emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Conversion methods
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        return dto;
    }
    
    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        return employee;
    }
}