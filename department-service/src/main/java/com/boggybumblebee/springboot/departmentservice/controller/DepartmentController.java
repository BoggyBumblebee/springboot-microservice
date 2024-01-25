package com.boggybumblebee.springboot.departmentservice.controller;


import com.boggybumblebee.springboot.departmentservice.client.EmployeeClient;
import com.boggybumblebee.springboot.departmentservice.model.Department;
import com.boggybumblebee.springboot.departmentservice.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentRepository repository;
    private final EmployeeClient employeeClient;

    public DepartmentController(DepartmentRepository repository, EmployeeClient employeeClient) {
        this.repository = repository;
        this.employeeClient = employeeClient;
    }

    @Bean
    CommandLineRunner initDepartments(DepartmentRepository repo) {
        return args -> {
            if (repo.findAll().isEmpty()) {
                repo.save(new Department(1L, "HR"));
                repo.save(new Department(2L, "Finance"));
                repo.save(new Department(3L, "Operations"));
                repo.save(new Department(4L, "Support"));
                repo.save(new Department(5L, "Development"));
                repo.save(new Department(6L, "Sales"));
            }
        };
    }

    @PostMapping
    public Department add(@RequestBody Department department) {
        return repository.save(department);
    }

    @GetMapping
    public List<Department> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/with-employees")
    public List<Department> findAllWithEmployees() {
        List<Department> departments = repository.findAll();
        departments.forEach(department -> department.setEmployees(employeeClient.findByDepartment(department.getId())));
        return departments;
    }
}