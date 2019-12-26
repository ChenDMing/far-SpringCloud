package own.auth.service;

import own.auth.entity.po.AdminUser;

public interface AdminUserService {

    AdminUser getAdminUserByUsername(String username);

}
