package com.moving.admin.service;

import com.moving.admin.dao.customer.CustomerDao;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.dao.customer.DepartmentDao;
import com.moving.admin.dao.folder.FolderItemDao;
import com.moving.admin.dao.talent.*;
import com.moving.admin.entity.customer.Department;
import com.moving.admin.entity.folder.FolderItem;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.talent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TalentService extends AbstractService {

    @Autowired
    private TalentDao talentDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    private TalentRemindDao talentRemindDao;

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private ChanceDao chanceDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private ExperienceDao experienceDao;

    @Autowired
    private FolderItemDao folderItemDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonService commonService;

    // 手机号验证重复
    public Talent checkPhone(String phone) {
        Talent talent = null;
        talent = talentDao.findTalentByPhone(phone);
        return talent;
    }

    // 分页查询
    public Page<Talent> getCustomerList(String city, String name, String industry, String aptness, Long folderId, Pageable pageable) {
        Page<Talent> result = talentDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(city)) {
                list.add(cb.equal(root.get("city"), city));
            }
            if (!StringUtils.isEmpty(aptness)) {
                list.add(cb.like(root.get("aptness"), "%" + aptness + "%"));
            }
            if (!StringUtils.isEmpty(name)) {
                list.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (!StringUtils.isEmpty(industry)) {
                list.add(cb.like(root.get("industry"), "%" + industry + "%"));
            }
            if (folderId != null) {
                List<FolderItem> folderItems = folderItemDao.findAllByFolderIdAndAndType(folderId, 2);
                List<Long> ids = new ArrayList<>();
                folderItems.forEach(folderItem -> {
                    ids.add(folderItem.getItemId());
                });
                Expression<Long> exp = root.<Long>get("id");
                list.add(cb.and(exp.in(ids)));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
        return result;
    }

    // 添加人才
    @Transactional
    public Long save(Talent talent) {
        talent.setUpdateTime(new Date(System.currentTimeMillis()));
        talentDao.save(talent);
        Long id = talent.getId();
        experienceDao.removeAllByTalentId(id);
        talent.getExperienceList().forEach(item -> {
            Long customerId = addCustomerFromTalentInfo(item.getCompany());
            Long departmentId = commonService.addDepartmentFromTalentInfo(customerId, item.getDepartment());
            Experience experience = experienceDao.findExperienceByCustomerIdAndDepartmentIdAndTalentId(item.getCustomerId(), item.getDepartmentId(), id);
//            if (experience == null) {
            if (true) {
                item.setTalentId(id);
                item.setCustomerId(customerId);
                item.setDepartmentId(departmentId);
                if (item.getStatus()) {
                    item.setEndTime(new Date(System.currentTimeMillis()));
                }
                experienceDao.save(item);
            }
        });
        friendDao.removeAllByTalentId(id);
        talent.getFriends().forEach(friend -> {
            Long customerId = addCustomerFromTalentInfo(friend.getCompany());
            Long departmentId = commonService.addDepartmentFromTalentInfo(customerId, friend.getDepartment());
            friend.setTalentId(id);
            friend.setCustomerId(customerId);
            friend.setDepartmentId(departmentId);
            friendDao.save(friend);
        });
        chanceDao.removeAllByTalentId(id);
        talent.getChances().forEach(chance -> {
            Long customerId = addCustomerFromTalentInfo(chance.getCompany());
            chance.setTalentId(id);
            chance.setCustomerId(customerId);
            chanceDao.save(chance);
        });
        TalentRemind remind = talent.getRemind();
        if (remind != null) {
            remind.setTalentId(id);
            talentRemindDao.save(remind);
        }
        return id;
    }

    // 添加客户，去重
    public Long addCustomerFromTalentInfo(String name) {
        Customer customer = customerDao.findByName(name);
        if (customer != null) {
            return customer.getId();
        } else {
            customer = new Customer();
            customer.setName(name);
            customer.setType(0);
            customerDao.save(customer);
            return customer.getId();
        }
    }

    // 获取人才详情
    public Talent getTalentById(Long id) {
        Talent talent = talentDao.findById(id).get();
        Long userId = talent.getCreateUserId();
        if (userId != null) {
            talent.setCreateUser(userDao.findById(userId).get().getNickName());
        }
        if (talent != null) {
            List<Experience> experienceList = experienceDao.findAllByTalentId(id);
            experienceList.forEach(experience -> {
                if (experience.getCustomerId() != null)
                    experience.setCompany(customerDao.findById(experience.getCustomerId()).get().getName());
                if (experience.getDepartmentId() != null)
                    experience.setDepartment(departmentDao.findById(experience.getDepartmentId()).get().getName());
            });
            talent.setExperienceList(experienceList);
            List<Friend> friendList = friendDao.findAllByTalentId(id);
            friendList.forEach(friend -> {
                if (friend.getCustomerId() != null) {
                    friend.setCompany(customerDao.findById(friend.getCustomerId()).get().getName());
                }
                if (friend.getDepartmentId() != null) {
                    friend.setDepartment(departmentDao.findById(friend.getDepartmentId()).get().getName());
                }
            });
            talent.setFriends(friendList);
            List<Chance> chanceList = chanceDao.findAllByTalentId(id);
            chanceList.forEach(chance -> {
                if (chance.getCustomerId() != null)
                    chance.setCompany(customerDao.findById(chance.getCustomerId()).get().getName());
            });
            talent.setChances(chanceList);
        }
        return talent;
    }

    // 获取人才跟踪记录
    public List<TalentRemind> getAllRemind(Long id) {
        List<TalentRemind> list = talentRemindDao.findAllByTalentIdOrderByIdDesc(id);
        list.forEach(talentRemind -> {
            Long userId = talentRemind.getCreateUserId();
            if (userId != null) {
                User user = userDao.findById(userId).get();
                talentRemind.setCreateUser(user != null ? user.getNickName() : "");
            }
        });
        return list;
    }

    // 关注装修改
    public void toggleFollow(Long id, Boolean follow) {
        Talent talent = talentDao.findById(id).get();
        talent.setFollow(follow);
        talentDao.save(talent);
    }

    // 添加客户跟踪
    public Long saveRemind(TalentRemind remind) {
        remind.setCreateTime(new Date(System.currentTimeMillis()));
        remind.setUpdateTime(new Date(System.currentTimeMillis()));
        talentRemindDao.save(remind);
        Long followId = remind.getFollowRemindId();
        if (followId != null) {
            finishRemindById(followId);
        }
        return remind.getId();
    }

    // 客户跟踪跟进结束
    public void finishRemindById(Long id) {
        TalentRemind talentRemind = talentRemindDao.findById(id).get();
        talentRemind.setFinish(true);
        talentRemindDao.save(talentRemind);
    }
}
