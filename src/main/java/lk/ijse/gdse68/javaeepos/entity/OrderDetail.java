package lk.ijse.gdse68.javaeepos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail {

    private String order_id;
    private String item_code;
    private BigDecimal unit_price;
    private int qty;

    public OrderDetail(String item_code, BigDecimal unit_price, int qty) {
        this.item_code = item_code;
        this.unit_price = unit_price;
        this.qty = qty;
    }
}
