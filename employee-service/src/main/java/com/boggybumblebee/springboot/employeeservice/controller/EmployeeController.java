package com.boggybumblebee.springboot.employeeservice.controller;

import com.boggybumblebee.springboot.employeeservice.model.Employee;
import com.boggybumblebee.springboot.employeeservice.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Bean
    CommandLineRunner initEmployees(EmployeeRepository repo) {
        return args -> {
            if (repo.findAll().isEmpty()) {
                repo.save(new Employee(1L, 1L, "Bert Baxter", 65, "Head of HR"));
                repo.save(new Employee(2L, 1L, "Mindy Mook", 65, "HR Associate"));
                repo.save(new Employee(3L, 2L, "Scrooge McDuck", 85, "CFO"));
                repo.save(new Employee(4L, 2L, "Bob Cratchit", 32, "Clerk"));
                repo.save(new Employee(5L, 3L, "Adrian Lamo", 85, "Red Team Member"));
                repo.save(new Employee(6L, 3L, "Kevin Mitnick", 32, "Cyber Security"));
                repo.save(new Employee(7L, 4L, "Ada Lovelace", 85, "Chief Engineer"));
                repo.save(new Employee(8L, 5L, "Linus Torvalds", 32, "Intern"));
            }
        };
    }

    @PostMapping
    public Employee add(@RequestBody Employee employee) {
        LOGGER.info("Employee add: {}", employee);
        return repository.save(employee);
    }

    @GetMapping
    public List<Employee> findAll() {
        LOGGER.info("Employee find");
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable("id") Long id) {
        LOGGER.info("Employee find: id={}", id);
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/department/{departmentId}")
    public List<Employee> findByDepartment(@PathVariable("departmentId") Long departmentId) {
        LOGGER.info("Employee find: departmentId={}", departmentId);
        return repository.findByDepartmentId(departmentId);
    }

}