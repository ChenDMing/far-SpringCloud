package own.timer.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserSummaryVo {

    private String userName;

    private Integer feedType;

    private BigDecimal money;

}
