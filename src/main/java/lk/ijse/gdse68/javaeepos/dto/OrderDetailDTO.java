package lk.ijse.gdse68.javaeepos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDTO {

    String order_id;
    String item_code;
    BigDecimal unit_price;
    int qty;

    public OrderDetailDTO(String item_code, BigDecimal unit_price, int qty) {
        this.item_code = item_code;
        this.unit_price = unit_price;
        this.qty = qty;
    }
}
