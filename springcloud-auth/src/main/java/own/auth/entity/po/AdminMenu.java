package own.auth.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户的权限菜单表
 */
@Data
@TableName("dsc_admin_menu")
public class AdminMenu {

    @TableId(type = IdType.AUTO)
    private Integer menuId;
    //父菜单ID，一级菜单为0
    private Integer parentId;
    //菜单标题
    private String title;
    //菜单URL
    private String jump;
    //授权(多个用逗号分隔，如：user:list,user:create)
    private String code;
    //类型   0：目录   1：菜单   2：按钮
    private Integer type;
    //菜单图标
    private String icon;
    //排序
    private Integer orderNum;
    //名称，和url对应
    private String name;

}
