package rafael.projects.store.springcustomerservice.service;

import org.springframework.stereotype.Service;
import rafael.projects.store.springcustomerservice.entity.Customer;
import rafael.projects.store.springcustomerservice.entity.Region;

import java.util.List;


public interface CustomerService {

    public List<Customer> findCustomerAll();
    public List<Customer> findCustomerByRegion(Region region);

    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public Customer deleteCustomer(Customer customer);
    public Customer getCustomer(Long id);

}
