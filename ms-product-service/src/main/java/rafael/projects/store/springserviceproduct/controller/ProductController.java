package rafael.projects.store.springserviceproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rafael.projects.store.springserviceproduct.entity.Category;
import rafael.projects.store.springserviceproduct.entity.Product;
import rafael.projects.store.springserviceproduct.service.ProductService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(name="category_id", required = false) Long categoryId){
        List<Product> products = new ArrayList<>();
        if (null == categoryId) {
            products = productService.listAllProduct();
            if(products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }
        else {
            //Se construye la categoria con builder
            products = productService.findByCategory(Category.builder().id(categoryId).build());
            if(products.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        if(null == product){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody Product product, BindingResult result) throws JsonProcessingException {
        if(result.hasErrors()) {
            //return new ResponseEntity(this.formatMessage(result), HttpStatus.BAD_REQUEST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Product productCreate = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        product.setId(id);
        Product productDB = productService.updateProduct(product);
        if(productDB == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        Product productDelete = productService.deleteProduct(id);
        if(productDelete == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDelete);
    }

    //Actualizar el stock
    @GetMapping(value = "/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable Long id, @RequestParam(name = "quantity", required = true) Double quantity){
        Product product = productService.updateStock(id, quantity);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }


    //Para formatear los mensajes
    private String formatMessage(BindingResult bindingResult) throws JsonProcessingException {
        List<Map<String, String>> errors = bindingResult.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }

}

















