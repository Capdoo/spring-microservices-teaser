package rafael.projects.store.springshoppingservice.shoppingservice.models;

import lombok.Data;

@Data
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double stock;
    private Double price;
    private String status;
    private Category category;

}
