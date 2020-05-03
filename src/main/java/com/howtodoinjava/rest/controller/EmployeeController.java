package com.howtodoinjava.rest.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.howtodoinjava.rest.dao.EmployeeDAO;
import com.howtodoinjava.rest.model.Employee;
import com.howtodoinjava.rest.model.Employees;

class Cust {
    String name;

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "{" + " name='" + getName() + "'" + "}";
    }

    Cust(String name) {
        this.name = name;
    }
}

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    @Autowired
    private EmployeeDAO employeeDao;

    @GetMapping(path = "/", produces = "application/json")
    public Employees getEmployees() {
        Gson gson = new Gson();
        String result = gson.toJson(new Cust("yogendra"));
        System.out.println("result is" + result);
        Cust cc = gson.fromJson(result, Cust.class);
        System.out.println(cc.toString());
        return employeeDao.getAllEmployees();
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addEmployee(
            @RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
            @RequestHeader(name = "X-COM-LOCATION", required = false, defaultValue = "ASIA") String headerLocation,
            @RequestBody Employee employee) throws Exception {
        // Generate resource id
        Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
        employee.setId(id);

        // add resource
        employeeDao.addEmployee(employee);

        // Create resource location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId())
                .toUri();

        // Send location in response
        return ResponseEntity.created(location).build();
    }
}