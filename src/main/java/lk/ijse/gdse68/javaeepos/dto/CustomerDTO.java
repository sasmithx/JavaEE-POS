package lk.ijse.gdse68.javaeepos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {

    private String id;
    private String name;
    private String address;
    private String contact;
}
