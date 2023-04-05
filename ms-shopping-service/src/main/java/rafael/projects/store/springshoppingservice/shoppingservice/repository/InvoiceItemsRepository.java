package rafael.projects.store.springshoppingservice.shoppingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafael.projects.store.springshoppingservice.shoppingservice.entity.InvoiceItem;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem, Long> {
}
