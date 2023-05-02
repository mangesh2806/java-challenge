package jp.co.axa.apidemo;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(EmployeeController.class)
@ActiveProfiles("test")
public class EmployeeControllerTest {
	
	
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private EmployeeController employeeController;

	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}
	
	@Test
	public void getAllEmployess() throws Exception {
		String uri = "/api/v1?pageNo=0&pageSize=10&sortBy=id";
		Employee employee = new Employee();
		employee.setDepartment("IT");
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Employee> employees = Arrays
					.asList(mapper.readValue(Paths.get("src/test/resources/employeeList.json").toFile(), Employee[].class));
			
			given(employeeController.getAllEmployees("0", "10", "id")).willReturn(new ResponseEntity<List<Employee>>(employees, new HttpHeaders(), HttpStatus.OK));

			mvc.perform(get(uri).contentType(APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(2)))
					.andExpect(jsonPath("$[0].department", is(employee.getDepartment())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	@Test
	public void saveEmployeeTest() {
		Employee employee = new Employee();
		employee.setId(new Long(999));
		employee.setDepartment("IT");
		employee.setName("AXATESTNAME");
		employee.setSalary(999999);
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.post("/api/v1/employees")
					.accept(MediaType.APPLICATION_JSON).content(new String(Files.readAllBytes(Paths.get("src/test/resources/employee.json"))))
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mvc.perform(requestBuilder).andReturn();

			MockHttpServletResponse response = result.getResponse();

			assertEquals(HttpStatus.OK.value(), response.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateEmployeeTest() {
		Employee employee = new Employee();
		employee.setId(new Long(999));
		employee.setDepartment("IT");
		employee.setName("AXATESTNAME1");
		employee.setSalary(999999);
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.put("/api/v1/employees/999")
					.accept(MediaType.APPLICATION_JSON).content(new String(Files.readAllBytes(Paths.get("src/test/resources/employee.json"))))
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mvc.perform(requestBuilder).andReturn();

			MockHttpServletResponse response = result.getResponse();

			assertEquals(HttpStatus.OK.value(), response.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deleteEmployeeTest() {
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders
					.put("/api/v1/employees/999")
					.accept(MediaType.APPLICATION_JSON).content(new String(Files.readAllBytes(Paths.get("src/test/resources/employee.json"))))
					.contentType(MediaType.APPLICATION_JSON);

			MvcResult result = mvc.perform(requestBuilder).andReturn();

			MockHttpServletResponse response = result.getResponse();

			assertEquals(HttpStatus.OK.value(), response.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
