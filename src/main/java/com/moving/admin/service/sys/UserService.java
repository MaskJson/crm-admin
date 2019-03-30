package com.moving.admin.service.sys;

import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.sys.Permission;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.service.AbstractService;
import com.moving.admin.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends AbstractService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    // 登录
    public User login(String username, String password) {
        List<User> list = userDao.login(username, password.toUpperCase());
        if (list.size() > 0) {
            User user = (User)list.get(0);
            User result = new User();
            result.setId(user.getId());
            result.setNickName(user.getNickName());
            result.setAvatar(user.getAvatar());
            result.setStatus(user.getStatus());
            result.setRoleId(user.getRoleId());
            return result;
        }
        return null;
    }

    // 根据roleId获取user
    public User findUserByRoleId(Long roleId) {
        return userDao.findByRoleId(roleId);
    }

    // 分页查询用户列表
    public Page<User> getUserByPage(String username, Integer type, String startDate, String endDate, Pageable pageable) {
        return userDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(username)) {
                list.add(cb.equal(root.get("username"), username));
            }
            if (type != null) {
                list.add(cb.equal(root.get("type"), type));
            }
//            if (user.getStatus() != null) {
//                list.add(cb.equal(root.get("status"), user.getStatus()));
//            }
            if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                Date start = DateUtil.strToDate(startDate);
                Date end = DateUtil.strToDate(endDate);
                list.add(cb.between(root.get("createTime").as(Date.class), start, end));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
    }

    // 根据username获取用户详情
    public User findByUsername(String userName) {
        User mgrUser = userDao.findByUsername(userName);
        if (mgrUser != null) {
            Long userId = mgrUser.getId();
            List<Permission> permissionList = permissionService.findPermissionsOfUser();
            mgrUser.setPermissions(permissionList);
        }
        return mgrUser;
    }

    // 根据userID获取用户详情
    public User findByUserId() {
        User mgrUser = userDao.findById(super.getCurrentUserId()).get();
        Long userId = mgrUser.getId();
        List<Permission> permissionList = permissionService.findPermissionsOfUser();
        mgrUser.setPermissions(permissionList);
        return mgrUser;
    }

    public User save(User mgrUser) {
        return userDao.save(mgrUser);
    }

    public User get(Long id) {
        return userDao.findById(id).get();
    }

    public User update(User u) {
        return userDao.save(u);
    }

    public void delete(Long id) {
        userDao.deleteById(id);
    }
}
