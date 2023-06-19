package aclue.auth0_session_backend.persistance.repository;


import aclue.auth0_session_backend.persistance.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
