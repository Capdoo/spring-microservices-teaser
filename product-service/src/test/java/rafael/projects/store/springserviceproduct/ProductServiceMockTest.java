package rafael.projects.store.springserviceproduct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rafael.projects.store.springserviceproduct.entity.Category;
import rafael.projects.store.springserviceproduct.entity.Product;
import rafael.projects.store.springserviceproduct.repository.ProductRepository;
import rafael.projects.store.springserviceproduct.service.ProductService;
import rafael.projects.store.springserviceproduct.service.ProductServiceImpl;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(productRepository);
        Product product = Product.builder()
                .name("Monitor")
                .category(Category.builder().id(1L).build())
                .price(Double.parseDouble("12.50"))
                .stock(Double.parseDouble("5"))
                .build();
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
    }

    @Test
    public void whenValidGetID_ThenReturnProduct(){

        Product found = productService.getProduct(1L);
        Assertions.assertEquals(found.getName(), "Monitor");

    }

    @Test
    public void whenValidUpdateStock_ThenReturnNewStock(){
        Product newStock = productService.updateStock(1L, Double.parseDouble("8"));
        //Product found = productService.getProduct(1L);
        Assertions.assertEquals(newStock.getStock(),13);
    }

}










