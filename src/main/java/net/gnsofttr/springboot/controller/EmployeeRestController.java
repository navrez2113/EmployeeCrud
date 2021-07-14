package net.gnsofttr.springboot.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.gnsofttr.springboot.model.Employee;
import net.gnsofttr.springboot.repository.EmployeeRepository;

@RestController
@RequestMapping("/rest")
public class EmployeeRestController {

	@Autowired
	private EmployeeRepository employeeRepository;
	

	@Cacheable("allEmployees")
	@RequestMapping(method = RequestMethod.GET, value = "/employees")
	public ResponseEntity<List<Employee>> getEmployees() {
		System.out.println(">>>> inside getEmployees...");
		List<Employee> employees = employeeRepository.findAll();
		return ResponseEntity.ok(employees);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/employees")
	public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
		try {
			employeeRepository.save(employee);
			//Long id = employee.getId();
		//	URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
			return ResponseEntity.status(HttpStatus.OK).build();
					
		} catch (ConstraintViolationException cv) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/employees/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeRequest) {
		try {
			Employee employee = employeeRepository.getOne(id);
			employee.setFirstName(employeeRequest.getFirstName());
			employee.setLastName(employeeRequest.getLastName());
			employeeRepository.save(employee);
			return ResponseEntity.ok().build();
		} catch (ConstraintViolationException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/employees/delete/{id}")
	public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes) {
		
		employeeRepository.deleteById(id);
		  redirectAttributes.addFlashAttribute("message", "employee deleted with id: " + id);
		return "redirect:/employees";
	}
	
    @RequestMapping(method = RequestMethod.GET, value = "/employee")
    public ResponseEntity<List<Employee>> getBylastName(@RequestParam("ln") String lastName) {
        List<Employee> employee = employeeRepository.findBylastName(lastName);
        return ResponseEntity.ok(employee);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/employee/{id}")
    public ResponseEntity<Optional<Employee>> getEmployees(@PathVariable("id") Long id) {
    	
    	Optional<Employee> employee = employeeRepository.findById(id);
		return ResponseEntity.ok(employee);
    }

    	
    	
    }
    

