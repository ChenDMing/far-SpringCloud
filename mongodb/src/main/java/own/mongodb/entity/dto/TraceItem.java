package own.mongodb.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TraceItem implements Serializable {

    private String date;

    private String activity;

    private String location;

}
