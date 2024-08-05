package lk.ijse.gdse68.javaeepos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {

    private String code;
    private String name;
    private BigDecimal price;
    private int qty;
}
