package rafael.projects.store.springserviceproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafael.projects.store.springserviceproduct.entity.Category;
import rafael.projects.store.springserviceproduct.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllByCategory(Category category);

}
