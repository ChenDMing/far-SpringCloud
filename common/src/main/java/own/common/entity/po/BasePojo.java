package own.common.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BasePojo {

    Date createTime;

    Date updateTime;

}
