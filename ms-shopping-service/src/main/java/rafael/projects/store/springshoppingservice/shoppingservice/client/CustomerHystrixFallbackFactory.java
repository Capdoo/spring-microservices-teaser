package rafael.projects.store.springshoppingservice.shoppingservice.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import rafael.projects.store.springshoppingservice.shoppingservice.models.Customer;

@Component
public class CustomerHystrixFallbackFactory implements CustomerClient{

    @Override
    public ResponseEntity<Customer> getCustomer(Long id) {
        Customer customer = Customer.builder()
                .firstName("none")
                .lastName("none")
                .email("none")
                .photoUrl("none").build();
        return ResponseEntity.ok(customer);
    }
}
