package rafael.projects.store.springserviceproduct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rafael.projects.store.springserviceproduct.entity.Category;
import rafael.projects.store.springserviceproduct.entity.Product;
import rafael.projects.store.springserviceproduct.repository.ProductRepository;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class ProductRepositoryMockTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByCategory_thenReturnListProducts(){
        Product product01 = Product.builder()
                .name("Computer")
                .category(Category.builder().id(1L).build())
                .description("Blank desc")
                .stock(Double.parseDouble("10"))
                .price(Double.parseDouble("1240.99"))
                .status("Created")
                .createAt(new Date()).build();
        productRepository.save(product01);

        List<Product> founds = productRepository.findAllByCategory(product01.getCategory());

        Assertions.assertEquals(founds.size(), 3);

    }

}
