package com.boggybumblebee.springboot.employeeservice.repository;

import com.boggybumblebee.springboot.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentId(Long departmentId);
}
