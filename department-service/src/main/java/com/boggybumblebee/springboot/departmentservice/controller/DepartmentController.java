package com.boggybumblebee.springboot.departmentservice.controller;


import com.boggybumblebee.springboot.departmentservice.client.EmployeeClient;
import com.boggybumblebee.springboot.departmentservice.model.Department;
import com.boggybumblebee.springboot.departmentservice.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private EmployeeClient employeeClient;

    @Bean
    CommandLineRunner initDepartments(DepartmentRepository repo) {
        return args -> {
            if (repo.findAll().isEmpty()) {
                repo.addDepartment(new Department(1L, "HR"));
                repo.addDepartment(new Department(2L, "Finance"));
                repo.addDepartment(new Department(3L, "Operations"));
                repo.addDepartment(new Department(4L, "Support"));
                repo.addDepartment(new Department(5L, "Development"));
                repo.addDepartment(new Department(6L, "Sales"));
            }
        };
    }

    @PostMapping
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department add: {}", department);
        return repository.addDepartment(department);
    }

    @GetMapping
    public List<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id);
    }

    @GetMapping("/with-employees")
    public List<Department> findAllWithEmployees() {
        LOGGER.info("Department find");
        List<Department> departments = repository.findAll();
        departments.forEach(department -> department.setEmployees(employeeClient.findByDepartment(department.getId())));
        return departments;
    }

}