package aclue.auth0_session_backend.service;

import aclue.auth0_session_backend.body.CustomerBody;
import aclue.auth0_session_backend.persistance.model.Customer;
import aclue.auth0_session_backend.persistance.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        customer.setFirstname(body.getFirstname());
        customer.setLastname(body.getLastname());
        customer.setEmail(body.getEmail());
        customer.setStreet(body.getStreet());
        customer.setHousingNumber(body.getHousingNumber());
        customer.setPostalCode(body.getPostalCode());
        customer.setCity(body.getCity());
        customer.setAdditionalInformation(body.getAdditionalInformation());

        this.customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        return (List<Customer>) this.customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return this.customerRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public void editCustomer(Customer customer) {
        this.customerRepository.save(customer);
    }

    public void deleteCustomerById(Long id) {
        this.customerRepository.deleteById(id);
    }
}
