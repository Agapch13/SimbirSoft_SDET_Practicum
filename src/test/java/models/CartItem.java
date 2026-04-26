package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartItem {

    private String name;
    private Double price;
    private Integer quantity;
}
