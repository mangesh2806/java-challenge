package jp.co.axa.apidemo.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import jp.co.axa.apidemo.entities.Employee;

@Component("employeeDataValidator")
public class EmployeeDataValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		
		 return Employee.class.equals(clazz);
	}

	@Override
    public void validate(Object obj, Errors errors) {
        Employee employee = (Employee) obj;
        if (checkInputString(employee.getName())) {
            errors.rejectValue("name", "name.empty");
        }
   
        if (checkInputString(employee.getDepartment())) {
            errors.rejectValue("department", "department.empty");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
