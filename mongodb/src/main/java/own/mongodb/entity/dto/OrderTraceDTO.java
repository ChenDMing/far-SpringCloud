package own.mongodb.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderTraceDTO implements Serializable {

    private Integer awb;

    private String current_status;

    private String current_timestamp;

    private String order_id;

    private String etd;

    private List<TraceItem> scans;

}
