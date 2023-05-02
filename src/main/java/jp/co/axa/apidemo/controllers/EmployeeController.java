package jp.co.axa.apidemo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.axa.apidemo.dto.Error;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.validator.EmployeeDataValidator;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	private static final String EMPLOYEE_NOT_FOUND_FOR_ID = "Employee Not Found For ID:";

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeDataValidator employeeDataValidator;

	@InitBinder("employee")
	public void initMerchantOnlyBinder(WebDataBinder binder) {
		binder.addValidators(employeeDataValidator);
	}

	/**
	 * Fetch Employees based on pagination if parameters are provided or else fetch all employess
	 * 
	 * @return List<Employee>
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 */
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(name = "pageNo") String pageNo,
			@RequestParam(name = "pageSize") String pageSize, @RequestParam(name = "sortBy") String sortBy) {
		List<Employee> list = employeeService.retrieveEmployees(pageNo, pageSize, sortBy);
		if (list.isEmpty()) {
			return new ResponseEntity<List<Employee>>(list, new HttpHeaders(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Employee>>(list, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Fetch Single Employee based on Employee Id
	 * 
	 * @return Employee
	 * @param employeeId
	 */
	@GetMapping("/employees/{employeeId}")
	public ResponseEntity getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		Employee employee = employeeService.getEmployee(employeeId);
		if (employee == null) {
			Error error = new Error(HttpStatus.NOT_FOUND.value(),EMPLOYEE_NOT_FOUND_FOR_ID+employeeId);
			return new ResponseEntity<Error>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Employee>(employee, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Save Employee
	 * @return void 
	 * @param Employee
	 */
	@PostMapping("/employees")
	public void saveEmployee(@Valid @RequestBody Employee employee) {
		employeeService.saveOrUpdateEmployee(employee);
	}

	/**
	 * Delete Employee based on employeeId
	 * @return void
	 * @param Employee
	 */
	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		Employee employee = employeeService.getEmployee(employeeId);
		if (employee == null) {
			Error error = new Error(HttpStatus.NOT_FOUND.value(),EMPLOYEE_NOT_FOUND_FOR_ID+employeeId);
			return new ResponseEntity<Error>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
		}
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Update Employee based on employeeId
	 * @return void
	 * @param Employee
	 */
	@PutMapping("/employees/{employeeId}")
	public ResponseEntity updateEmployee(@RequestBody Employee employee,
			@PathVariable(name = "employeeId") Long employeeId) {

		if (employee != null) {
			employeeService.saveOrUpdateEmployee(employee);
			return new ResponseEntity(HttpStatus.OK);
		}

		Error error = new Error(HttpStatus.NOT_FOUND.value(),EMPLOYEE_NOT_FOUND_FOR_ID+employeeId);
		return new ResponseEntity<Error>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}

}
