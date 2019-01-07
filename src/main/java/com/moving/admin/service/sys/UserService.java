package com.moving.admin.service.sys;

import com.moving.admin.entity.sys.User;
import com.moving.admin.dao.sys.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User login(String username, String password) {
        List<User> list = userDao.login(username, password);
        if (list.size() > 0) {
            User user = (User)list.get(0);
            User result = new User();
            result.setId(user.getId());
            result.setNickName(user.getNickName());
            result.setAvatar(user.getAvatar());
            result.setRoleId(user.getRoleId());
            return result;
        }

        return null;
    }

}
