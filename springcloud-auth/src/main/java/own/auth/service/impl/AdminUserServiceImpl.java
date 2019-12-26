package own.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import own.auth.dao.AdminUserMapper;
import own.auth.entity.po.AdminUser;
import own.auth.service.AdminUserService;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser getAdminUserByUsername(String username) {
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return adminUserMapper.selectOne(queryWrapper);
    }
}
