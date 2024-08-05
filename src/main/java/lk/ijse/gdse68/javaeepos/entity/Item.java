package lk.ijse.gdse68.javaeepos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {

    private String code;
    private String name;
    private BigDecimal price;
    private int qty;

    public Item(String code, int qty) {
        this.code=code;
        this.qty=qty;
    }
}
