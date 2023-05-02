package jp.co.axa.apidemo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@Service
@CacheConfig(cacheNames = {"employeeCache"})
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> retrieveEmployees(String pageNo, String pageSize, String sortBy)
    {
    	if(StringUtils.isEmpty(sortBy) || StringUtils.isEmpty(pageNo) || StringUtils.isEmpty(pageSize)) {
    		
    		return employeeRepository.findAll();
    	}
        Pageable paging = PageRequest.of(Integer.parseInt(pageNo), Integer.parseInt(pageSize), Sort.by(sortBy));
 
        Page<Employee> pagedResult = employeeRepository.findAll(paging);
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Employee>();
        }
    }
    
    @Cacheable("employee")
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.isPresent() ? optEmp.get() : null;
       
    }

    public void saveOrUpdateEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    
}