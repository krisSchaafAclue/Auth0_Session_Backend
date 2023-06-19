package aclue.auth0_session_backend.controller;

import aclue.auth0_session_backend.body.CustomerBody;
import aclue.auth0_session_backend.persistance.model.Customer;
import aclue.auth0_session_backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(Api.CUSTOMERS_PATH)
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addCustomer(@RequestBody CustomerBody body) {
        this.customerService.addCustomer(body);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Customer> getCustomers() {
        return this.customerService.getCustomers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Customer getCustomerById(@PathVariable Long id) {
        return this.customerService.findById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editCustomer(@RequestBody Customer customer) {
        this.customerService.editCustomer(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerById(@PathVariable Long id) {
        this.customerService.deleteCustomerById(id);
    }
}
