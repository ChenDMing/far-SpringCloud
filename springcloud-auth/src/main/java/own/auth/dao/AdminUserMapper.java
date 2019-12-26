package own.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import own.auth.entity.po.AdminUser;

@Repository
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
