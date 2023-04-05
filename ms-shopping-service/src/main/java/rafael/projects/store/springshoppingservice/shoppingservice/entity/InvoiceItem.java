package rafael.projects.store.springshoppingservice.shoppingservice.entity;

import lombok.Data;
import rafael.projects.store.springshoppingservice.shoppingservice.models.Product;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "tbl_invoice_items")
@Data
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "El stock debe ser mayor que cero")
    private Double quantity;
    private Double price;

    @Column(name = "product_id")
    private Long productId;

    @Transient
    private Double subTotal;

    @Transient
    private Product product;

    public Double getSubTotal(){
        if(this.price > 0 && this.quantity > 0){
            return this.price*this.quantity;
        }else{
            return (double) 0;
        }
    }

    public InvoiceItem() {
        this.quantity = 0.0;
        this.price = 0.0;
    }
}
