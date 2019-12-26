package own.auth.entity.po;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import own.common.entity.po.BasePojo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户表
 */
@Data
@TableName("dsc_admin_user")
public class AdminUser extends BasePojo {

    @TableId(type = IdType.AUTO)
    private Integer userId;
    //用户名
    private String username;
    //密码
    private String icon;

    private String password;
    //昵称
    private String nickname;
    //邮箱
    private String email;
    //用户电话号码
    private String telephone;
    //状态  0：禁用   1：正常
    private Integer status;
    //用户关联的商家id
    private Integer shopId;

    private String shopName;

    @TableField(exist=false)
    private Integer menuParentId;
    //创建者
    private String createUser;

    private String whatsapp;

    @TableField(exist=false)
    private String accessToken;

    @TableField(exist=false)
    private Map<Integer, Set<String>> anniu;

    public String parseLoginCache(){
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(this);
        jsonObject.put("password", this.password);
        return jsonObject.toJSONString();
    }

    @TableField(exist = false)
    private String newPassword;

    @TableField(exist=false)
    private List<AdminMenu> pre;

    @TableField(exist = false)
    private JSONObject shop;

    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private String roleIds;

    @TableField(exist = false)
    private Integer roleId;

    private Integer isDelete;

}
