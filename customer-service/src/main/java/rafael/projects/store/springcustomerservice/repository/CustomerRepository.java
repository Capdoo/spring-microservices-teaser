package rafael.projects.store.springcustomerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafael.projects.store.springcustomerservice.entity.Customer;
import rafael.projects.store.springcustomerservice.entity.Region;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findByNumberID(String numberID);
    public List<Customer> findByLastName(String lastName);
    public List<Customer> findByRegion(Region region);

}


