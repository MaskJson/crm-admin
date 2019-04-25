package com.moving.admin.service.sys;

import com.moving.admin.dao.sys.TeamDao;
import com.moving.admin.entity.sys.Team;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.sys.Permission;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.exception.WebException;
import com.moving.admin.service.AbstractService;
import com.moving.admin.util.DateUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
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

    @Autowired
    private TeamDao teamDao;

    // 登录
    public User login(String username, String password) {
        List<User> list = userDao.login(username, password.toUpperCase());
        if (list.size() > 0) {
            User user = (User)list.get(0);
//            if (!user.getStatus()) {
//                throw new WebException(400, "用户已禁用，请联系管理员", null);
//            }
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
        if (mgrUser.getId() == null) {
            userDao.save(mgrUser);
//            if (mgrUser.getRoleId() == 3) {
//                Team team = new Team();
//                team.setLevel(1);
//                team.setUserId(mgrUser.getId());
//                teamDao.save(team);
//            }
        } else {
            User user = userDao.findById(mgrUser.getId()).get();
            if (user != null) {
                mgrUser.setPassword(user.getPassword());
            }
            mgrUser.setUpdateTime(new Date());
            userDao.save(mgrUser);
        }
        return mgrUser;
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

    public void toggleStatus(Long id, Integer status) {
        User user = userDao.findById(id).get();
        if (user != null) {
            user.setStatus(status);
            userDao.save(user);
        }
    }

    public List<User> getAllUser() {
        return userDao.findAll();
    }
}
