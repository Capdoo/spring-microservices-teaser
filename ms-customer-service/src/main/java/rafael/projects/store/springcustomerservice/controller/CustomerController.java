package rafael.projects.store.springcustomerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rafael.projects.store.springcustomerservice.entity.Customer;
import rafael.projects.store.springcustomerservice.entity.Region;
import rafael.projects.store.springcustomerservice.service.CustomerService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //List all customers
    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId", required = false) Long regionId){
        List<Customer> listCustomer = new ArrayList<>();
        if (null == regionId){
            listCustomer = customerService.findCustomerAll();

            if (listCustomer.isEmpty()){
                return ResponseEntity.noContent().build();
            }

        }else {
            Region region = new Region();
            region.setId(regionId);
            listCustomer = customerService.findCustomerByRegion(region);

            if (null == listCustomer){
                log.error("Customers with region id {} not found", regionId);
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(listCustomer);
    }

    //List single customer
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        log.info("Fetching Customer with id {}", id);
        Customer customer = customerService.getCustomer(id);
        if (null == customer){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    //Create a customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult){
        log.info("Creating Customer : {}", customer);
        if(bindingResult.hasErrors()){
            //return ResponseEntity.badRequest().build();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
        }
        Customer customerDB = customerService.createCustomer(customer);
        //return new ResponseEntity<Customer>(customerDB, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    //Update a customer
    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customer){
        log.info("Updating Customer with id: {}", id);
        Customer customerDB = customerService.getCustomer(id);
        if(null == customerDB){
            log.error("User not found id: {}", id);
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        customerDB = customerService.updateCustomer(customer);
        return ResponseEntity.ok(customerDB);
    }

    //Delete a customer
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") Long id){
        log.info("Deleting Customer with id: {}", id);
        Customer customerDB = customerService.getCustomer(id);
        if(null == customerDB){
            log.error("Unable to delete: User not found id: {}", id);
            return ResponseEntity.notFound().build();
        }
        customerDB.setId(id);
        customerDB = customerService.deleteCustomer(customerDB);
        return ResponseEntity.ok(customerDB);
    }

    //aux
    //Format messages
    private String formatMessage(BindingResult bindingResult){
        List<Map<String, String>> results = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> err = new HashMap<>();
                    err.put(fieldError.getCode(), fieldError.getDefaultMessage());
                    return err;
                }).collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(results).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jsonString;
    }



}













