package own.mongodb.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItem implements Serializable {

    private String name;

    private String sku;

    private String units;

    private String selling_price;

    public OrderItem(String name, String sku, String units, String selling_price) {
        this.name = name;
        this.sku = sku;
        this.units = units;
        this.selling_price = selling_price;
    }

}
