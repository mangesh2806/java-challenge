package jp.co.axa.apidemo.services;

import java.util.List;

import jp.co.axa.apidemo.entities.Employee;

public interface EmployeeService {

    public List<Employee> retrieveEmployees(String pageNo, String pageSize, String sortBy);

    public Employee getEmployee(Long employeeId);

    public void saveOrUpdateEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

}