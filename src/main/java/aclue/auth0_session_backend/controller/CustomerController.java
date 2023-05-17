package aclue.auth0_session_backend.controller;

import aclue.auth0_session_backend.Body.CustomerBody;
import aclue.auth0_session_backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/addCustomer")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCustomer(@RequestBody CustomerBody body) {
        this.customerService.addCustomer(body);
    }
}
