package com.interview.employee_management.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.employee_management.entity.Department;
import com.interview.employee_management.entity.Employee;
import com.interview.employee_management.repository.DepartmentRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;

@Service
public class ReportService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    public byte[] generateEmployeeReport() throws JRException {
        List<Department> departments = departmentRepository.findAllWithEmployees();
        
        // Create the main report design
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("EmployeeReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        jasperDesign.setColumnWidth(535);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(30);
        jasperDesign.setRightMargin(30);
        jasperDesign.setTopMargin(30);
        jasperDesign.setBottomMargin(30);
        
        // Define fields for the flattened data structure
        JRDesignField deptNameField = new JRDesignField();
        deptNameField.setName("departmentName");
        deptNameField.setValueClass(String.class);
        jasperDesign.addField(deptNameField);
        
        JRDesignField deptLocationField = new JRDesignField();
        deptLocationField.setName("departmentLocation");
        deptLocationField.setValueClass(String.class);
        jasperDesign.addField(deptLocationField);
        
        JRDesignField empIdField = new JRDesignField();
        empIdField.setName("employeeId");
        empIdField.setValueClass(String.class);
        jasperDesign.addField(empIdField);
        
        JRDesignField empNameField = new JRDesignField();
        empNameField.setName("employeeName");
        empNameField.setValueClass(String.class);
        jasperDesign.addField(empNameField);
        
        JRDesignField empEmailField = new JRDesignField();
        empEmailField.setName("employeeEmail");
        empEmailField.setValueClass(String.class);
        jasperDesign.addField(empEmailField);
        
        JRDesignField empPositionField = new JRDesignField();
        empPositionField.setName("employeePosition");
        empPositionField.setValueClass(String.class);
        jasperDesign.addField(empPositionField);
        
        JRDesignField empSalaryField = new JRDesignField();
        empSalaryField.setName("employeeSalary");
        empSalaryField.setValueClass(Double.class);
        jasperDesign.addField(empSalaryField);
        
        // Create title band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(50);
        
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText("Employee Report by Department");
        titleText.setX(0);
        titleText.setY(10);
        titleText.setWidth(535);
        titleText.setHeight(30);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setFontSize(Float.valueOf(18));
        titleText.setBold(Boolean.TRUE);
        titleBand.addElement(titleText);
        
        jasperDesign.setTitle(titleBand);
        
        // Create column header band
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(25);
        
        // Define column positions and widths
        int[] xPositions = {0, 80, 150, 250, 350, 450};
        int[] widths = {80, 70, 100, 100, 100, 85};
        String[] headers = {"Department", "Employee ID", "Name", "Email", "Position", "Salary"};
        
        for (int i = 0; i < headers.length; i++) {
            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setText(headers[i]);
            headerText.setX(xPositions[i]);
            headerText.setY(0);
            headerText.setWidth(widths[i]);
            headerText.setHeight(20);
            headerText.setBold(Boolean.TRUE);
            headerText.setFontSize(Float.valueOf(10));
            columnHeaderBand.addElement(headerText);
        }
        
        jasperDesign.setColumnHeader(columnHeaderBand);
        
        // Create detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        
        // Department name field
        JRDesignTextField deptField = new JRDesignTextField();
        deptField.setExpression(new JRDesignExpression("$F{departmentName}"));
        deptField.setX(xPositions[0]);
        deptField.setY(0);
        deptField.setWidth(widths[0]);
        deptField.setHeight(15);
        deptField.setFontSize(Float.valueOf(9));
        detailBand.addElement(deptField);
        
        // Employee ID field
        JRDesignTextField empIdTF = new JRDesignTextField();
        empIdTF.setExpression(new JRDesignExpression("$F{employeeId}"));
        empIdTF.setX(xPositions[1]);
        empIdTF.setY(0);
        empIdTF.setWidth(widths[1]);
        empIdTF.setHeight(15);
        empIdTF.setFontSize(Float.valueOf(9));
        detailBand.addElement(empIdTF);
        
        // Employee name field
        JRDesignTextField empNameTF = new JRDesignTextField();
        empNameTF.setExpression(new JRDesignExpression("$F{employeeName}"));
        empNameTF.setX(xPositions[2]);
        empNameTF.setY(0);
        empNameTF.setWidth(widths[2]);
        empNameTF.setHeight(15);
        empNameTF.setFontSize(Float.valueOf(9));
        detailBand.addElement(empNameTF);
        
        // Employee email field
        JRDesignTextField empEmailTF = new JRDesignTextField();
        empEmailTF.setExpression(new JRDesignExpression("$F{employeeEmail}"));
        empEmailTF.setX(xPositions[3]);
        empEmailTF.setY(0);
        empEmailTF.setWidth(widths[3]);
        empEmailTF.setHeight(15);
        empEmailTF.setFontSize(Float.valueOf(9));
        detailBand.addElement(empEmailTF);
        
        // Employee position field
        JRDesignTextField empPositionTF = new JRDesignTextField();
        empPositionTF.setExpression(new JRDesignExpression("$F{employeePosition}"));
        empPositionTF.setX(xPositions[4]);
        empPositionTF.setY(0);
        empPositionTF.setWidth(widths[4]);
        empPositionTF.setHeight(15);
        empPositionTF.setFontSize(Float.valueOf(9));
        detailBand.addElement(empPositionTF);
        
        // Employee salary field
        JRDesignTextField empSalaryTF = new JRDesignTextField();
        empSalaryTF.setExpression(new JRDesignExpression("\"$\" + $F{employeeSalary}"));
        empSalaryTF.setX(xPositions[5]);
        empSalaryTF.setY(0);
        empSalaryTF.setWidth(widths[5]);
        empSalaryTF.setHeight(15);
        empSalaryTF.setFontSize(Float.valueOf(9));
        detailBand.addElement(empSalaryTF);
        
        // CORRECT METHOD: Cast to JRDesignSection and add the band
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);
        
        // Compile the report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        
        // Prepare the data
        List<Map<String, Object>> reportData = prepareReportData(departments);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        
        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
        // Export to PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        
        return outputStream.toByteArray();
    }
    
    /**
     * Prepares flattened data structure for JasperReports
     */
    private List<Map<String, Object>> prepareReportData(List<Department> departments) {
        List<Map<String, Object>> reportData = new ArrayList<>();
        
        for (Department department : departments) {
            if (department.getEmployees().isEmpty()) {
                // Add department row even if no employees
                Map<String, Object> row = new HashMap<>();
                row.put("departmentName", department.getName());
                row.put("departmentLocation", department.getLocation());
                row.put("employeeId", "No employees");
                row.put("employeeName", "");
                row.put("employeeEmail", "");
                row.put("employeePosition", "");
                row.put("employeeSalary", 0.0);
                reportData.add(row);
            } else {
                // Add a row for each employee
                for (Employee employee : department.getEmployees()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("departmentName", department.getName());
                    row.put("departmentLocation", department.getLocation());
                    row.put("employeeId", employee.getId());
                    row.put("employeeName", employee.getName());
                    row.put("employeeEmail", employee.getEmail());
                    row.put("employeePosition", employee.getPosition());
                    row.put("employeeSalary", employee.getSalary());
                    reportData.add(row);
                }
            }
        }
        
        return reportData;
    }
}