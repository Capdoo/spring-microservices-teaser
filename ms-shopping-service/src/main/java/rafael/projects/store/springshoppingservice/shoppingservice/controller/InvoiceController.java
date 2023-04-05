package rafael.projects.store.springshoppingservice.shoppingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rafael.projects.store.springshoppingservice.shoppingservice.entity.Invoice;
import rafael.projects.store.springshoppingservice.shoppingservice.service.InvoiceService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> listAllInvoices(){
        List<Invoice> listInvoices = new ArrayList<>();
        listInvoices = invoiceService.findInvoiceAll();
        if (listInvoices.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listInvoices);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable(value = "id") Long id){
        log.info("Fetching invoice with id {}",id);
        Invoice invoiceDB = invoiceService.getInvoice(id);
        if (null == invoiceDB){
            log.error("Invoice with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoiceDB);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
        }
        Invoice invoiceDB = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable(value = "id") Long id, @RequestBody Invoice invoice){
        log.info("Updating invoice with id {}", id);
        Invoice invoiceDB = invoiceService.getInvoice(id);
        if (null == invoiceDB){
            return ResponseEntity.notFound().build();
        }
        invoice.setId(id);
        invoiceDB = invoiceService.updateInvoice(invoice);
        return ResponseEntity.ok(invoiceDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable(value = "id") Long id){
        log.info("Deleting invoice with id {}", id);
        Invoice invoiceDB = invoiceService.getInvoice(id);
        if (null == invoiceDB){
            return ResponseEntity.notFound().build();
        }
        invoiceDB = invoiceService.deleteInvoice(invoiceDB);
        return ResponseEntity.ok(invoiceDB);
    }

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
