package aclue.auth0_session_backend.service;

import aclue.auth0_session_backend.Body.CustomerBody;
import aclue.auth0_session_backend.model.Customer;
import aclue.auth0_session_backend.persistance.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void addCustomer(CustomerBody body) {
        Customer customer = new Customer();

        customer.setSalutation(body.getSalutation());
        customer.setFirstName(body.getFirstname());
        customer.setLastname(body.getLastname());
        customer.setEmail(body.getEmail());
        customer.setStreet(body.getStreet());
        customer.setHousingNumber(body.getHousingNumber());
        customer.setPostalCode(body.getPostalCode());
        customer.setCity(body.getCity());
        customer.setAdditionalInformation(body.getAdditionalInformation());

        this.customerRepository.save(customer);
    }
}
