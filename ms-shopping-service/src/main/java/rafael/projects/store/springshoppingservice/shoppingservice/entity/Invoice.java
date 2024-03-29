package rafael.projects.store.springshoppingservice.shoppingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import rafael.projects.store.springshoppingservice.shoppingservice.models.Customer;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_invoice")
    private String numberInvoice;

    private String description;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Valid
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> items;

    private String state;

    //Agregado en la seccion Feign
    @Transient
    private Customer customer;

    public Invoice(){
        items = new ArrayList<>();
    }

    //Que se registre la fecha autom. antes de insertar en bd
    //@PrePersist
    @PrePersist
    public void prePersist(){
        this.createAt = new Date();
    }

}







