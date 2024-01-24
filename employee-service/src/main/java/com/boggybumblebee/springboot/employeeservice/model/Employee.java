package com.boggybumblebee.springboot.employeeservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Employee {

    private @Id
    @GeneratedValue Long id;
    private Long departmentId;
    private String name;
    private int age;
    private String position;

    public Employee() {
    }

    public Employee(Long id, Long departmentId, String name, int age, String position) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.age = age;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return age == employee.age
                && Objects.equals(id, employee.id)
                && Objects.equals(departmentId, employee.departmentId)
                && Objects.equals(name, employee.name)
                && Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentId, name, age, position);
    }
}
