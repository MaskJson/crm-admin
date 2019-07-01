package com.moving.admin.service;

import com.moving.admin.dao.customer.*;
import com.moving.admin.dao.folder.FolderItemDao;
import com.moving.admin.dao.natives.CommonNative;
import com.moving.admin.dao.natives.CustomerNative;
import com.moving.admin.dao.project.ProjectDao;
import com.moving.admin.dao.sys.TeamDao;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.dao.talent.ExperienceDao;
import com.moving.admin.entity.customer.*;
import com.moving.admin.entity.folder.FolderItem;
import com.moving.admin.entity.sys.Team;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.talent.Experience;
import com.moving.admin.exception.WebException;
import com.moving.admin.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class CustomerService extends AbstractService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemindDao customerRemindDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private FolderItemDao folderItemDao;

    @Autowired
    private CustomerContactDao customerContactDao;

    @Autowired
    private CustomerContactRemarkDao customerContactRemarkDao;

    @Autowired
    private UnbindRecordDao unbindRecordDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CustomerNative customerNative;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private CommonNative commonNative;

    @Autowired
    private ExperienceDao experienceDao;

    @Autowired
    private TeamDao teamDao;

    // 添加、编辑
    public Long save(Customer customer) {
        if (customer.getId() != null) {
            customer.setUpdateTime(new Date(System.currentTimeMillis()));
        } else {
            Long roleId = customer.getRoleId();
            if (roleId != 3 && roleId != 1) {
                customer.setAuditType(0);
            }
        }
        customerDao.save(customer);
        return customer.getId();
    }

    // 查询（id、或 name）
    public Customer getCustomerByKey(Long id, String name) {
        Customer customer = null;
        if (id != null) {
            customer = customerDao.findById(id).get();
        } else {
            customer = customerDao.findByName(name);
        }
        if (customer != null) {
            Long userId = customer.getCreateUserId();
            Long followUserId = customer.getFollowUserId();
//            if (userId != null) {
//                customer.setCreateUser(userDao.findById(userId).get().getNickName());
//            }
            if (followUserId != null) {
                User user = userDao.findById(followUserId).get();
                if (user != null) {
                    customer.setFollowUser(user.getNickName());
                }
            }
        }
        return customer;
    }

    // 分页查询
    public Page<Customer> getCustomerList(Long id, String name, String industry, Long folderId, Boolean follow, Integer type, Long userId, Long roleId, Pageable pageable) {
        Page<Customer> result = customerDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("auditType"), 2));
            if (id != null) {
                list.add(cb.equal(root.get("id"), id));
            }
            if (!StringUtils.isEmpty(name)) {
                list.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (!StringUtils.isEmpty(industry)) {
                list.add(cb.like(root.get("industry"), "%" + industry + "%"));
            }
            if (follow != null) {
                list.add(cb.equal(root.get("follow"), follow));
            }
            if (userId != null && roleId != null && roleId > 1) {
                List<Long> ids = new ArrayList<>();
                ids.add(userId);
                if (roleId == 2 || roleId == 3 || roleId == 6 || roleId == 7) {
                    ids.addAll(commonNative.getMemberIds(userId, roleId));
                }
                Expression<Long> exp = root.<Long>get("followUserId");
                list.add(cb.and(exp.in(ids)));
                if (type != null) {
                    list.add(cb.equal(root.get("type"), type == 10 ? 1 : type));
                }
            }
            if (folderId != null) {
                List<FolderItem> folderItems = folderItemDao.findAllByFolderIdAndAndType(folderId, 1);
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
        result.forEach(customer -> {
            customer.setProjectCount(projectDao.getCountByCustomerId(customer.getId()));
            if (customer.getFollowUserId() != null) {
                customer.setUser(userDao.findById(customer.getFollowUserId()).get());
            }
        });
        return result;
    }

    // 关注装修改
    public void toggleFollow(Long id, Boolean follow) {
        Customer customer = customerDao.findById(id).get();
        if (customer != null) {
            customer.setFollow(follow);
            save(customer);
        }
    }

    // 添加客户跟踪
    public Long saveRemind(CustomerRemind remind) {
        remind.setCreateTime(new Date(System.currentTimeMillis()));
        remind.setUpdateTime(new Date(System.currentTimeMillis()));
        if (remind.getNextType() == null) {
            remind.setFinish(true);
        }
        customerRemindDao.save(remind);
        Long followId = remind.getFollowRemindId();
        if (followId != null) {
            List<Long> ids = new ArrayList<>();
            ids.add(followId);
            finishRemindByIds(ids);
        }
        Long customerId = remind.getCustomerId();
        Customer customer = customerDao.findById(customerId).get();
        if (customer != null) {
            // 签约后类型为客户
            Integer status = remind.getStatus();
            customer.setType(status == 5 ? 6 : status);
            if (status == 5) {
                customer.setContactTimeStart(remind.getContactTimeStart());
                customer.setContactTimeEnd(remind.getContactTimeEnd());
            }
            customerDao.save(customer);
        }
        return remind.getId();
    }

    // 客户跟踪跟进结束
    public void finishRemindByIds(List<Long> ids) {
        ids.forEach(id -> {
            CustomerRemind customerRemind = customerRemindDao.findById(id).get();
            customerRemind.setFinish(true);
            customerRemind.setUpdateTime(new Date(System.currentTimeMillis()));
            customerRemindDao.save(customerRemind);
        });
    }

    // 获取客户跟踪记录
    public List<CustomerRemind> getAllRemind(Long customerId) {
        List<CustomerRemind> list = customerRemindDao.findAllByCustomerIdOrderByIdDesc(customerId);
        list.forEach(customerRemind -> {
            Long userId = customerRemind.getCreateUserId();
            if (userId != null) {
                User user = userDao.findById(userId).get();
                if (user != null) {
                    customerRemind.setCreateUser(user.getNickName());
                }
            }
        });
        return list;
    }

    // 获取所有客户
    public List<Customer> getAllCustomer() {
        return customerDao.findAll();
    }

    // 获取所有部门
    public List<Department> getAllDepartment() {
        return departmentDao.findAll();
    }

    // 获取该公司下所有相关人才,talentId 去重
    public List<Map<String, Object>> getCustomerTalents(Long id) {
        return customerNative.getTalentsByCustomerId(id);
    }

    // 获取公司下所有部门
    public List<Department> getCustomerDepartments(Long id) {
        return departmentDao.findAllByCustomerId(id);
    }

    /**
     * 联系人相关
     */
    // 添加联系人
    public Long saveCustomerContact(CustomerContact customerContact) {
        if (customerContact.getId() == null) {
            customerContact.setCreateTime(new Date(System.currentTimeMillis()));
        }
        customerContact.setUpdateTime(new Date(System.currentTimeMillis()));
        customerContact.setDepartmentId(commonService.addDepartmentFromTalentInfo(customerContact.getCustomerId(), customerContact.getDepartment()));
        customerContactDao.save(customerContact);
        return customerContact.getId();
    }

    // 删除联系人及其联系记录
    @Transactional
    public void delCustomerContactByIds(Long [] ids) {
        customerContactDao.deleteAllByIdIn(ids);
        customerContactRemarkDao.deleteAllByCustomerContactIdIn(ids);
    }

    // 添加联系记录
    public CustomerContactRemark saveCustomerContactRemark(CustomerContactRemark customerContactRemark) {
        customerContactRemarkDao.save(customerContactRemark);
        return customerContactRemark;
    }

    // 获取客户下的所有联系人
    public List<Map<String, Object>> getAllCustomerContact(Long customerId, String name, Long departmentId, String position, String phone) {
        return customerNative.getAllCustomerContactById(customerId, name, departmentId, position, phone);
    }

    // 列名或取消列名
    @Transactional
    public Long toggleBindFollowUser(Long customerId, Long userId, Boolean status) {
        Customer customer = customerDao.findById(customerId).get();
        if (status && customer.getFollowUserId() != null) {
            throw new WebException(400, "该客户已被其他用户列名", null);
        }
        if (customer != null) {
            Long followUserId = customer.getFollowUserId();
            if (followUserId == userId) {
                UnbindRecord unbindRecord = new UnbindRecord();
                unbindRecord.setCustomerId(customerId);
                unbindRecord.setUserId(userId);
                unbindRecord.setCreateTime(new Date(System.currentTimeMillis()));
                unbindRecordDao.deleteAllByCustomerIdAndUserId(customerId, userId);
                unbindRecordDao.save(unbindRecord);
                customer.setType(0);
                customer.setFollowUserId(null);
            } else if (followUserId == null){
                UnbindRecord unbindRecord = unbindRecordDao.findUnbindRecordByCustomerIdAndUserIdAndCreateTimeAfter(customerId, userId, new Date(System.currentTimeMillis() - 2592000000L));
                if (unbindRecord != null) {
                    throw new WebException(400, "您在" + DateUtil.dateToStr(unbindRecord.getCreateTime()) + "取消了该客户的列名，一个月内不允许对该客户做列名操作", null);
                }
                Integer customersOfUser = customerDao.getCountByFollowUserId(userId);
                if (customersOfUser >= 100) {
                    throw new WebException(400, "单人的客户列名上限为100， 您已达到上限", null);
                } else {
                    customer.setType(1);
                    customer.setFollowUserId(userId);
                }
            }
            customerDao.save(customer);
            return customerId;
        } else {
            throw new WebException(400, "该客户不存在", null);
        }
    }

    // 获取达新建项目标准达公司，未签约先推人或者是客户
    public List<Customer> findProjectCustomers(Long userId) {
        List<Integer> types = new ArrayList<>();
        // 获取团队成员
        List<Long> ids = new ArrayList<>();
        Team team = teamDao.findTeamByUserId(userId);
        if (team != null) {
            Long id = team.getId();
            if (team.getTeamId() != null) {
                id = team.getTeamId();
            }
            List<Team> teams = teamDao.findAllByTeamIdOrId(id, id);
            teams.forEach(t -> {
                ids.add(t.getUserId());
            });
        }
        return customerDao.findAllByTypeAfterAndFollowUserIdIn(3, ids);
    }

    // 上传合同
    public void setContractUrl(Long customerId, String path) {
        Customer customer = customerDao.findById(customerId).get();
        if (customer != null) {
            customer.setContractUrl(path);
            customerDao.save(customer);
        }
    }

    public void auditCustomer(List<Long> ids, Integer auditType) {
        if (auditType == 1) {
            List<Customer> customers = customerDao.findAllById(ids);
            customerDao.deleteAll(customers);
        } else {
            ids.forEach(id -> {
                Customer customer = customerDao.findById(id).get();
                if (customer != null) {
                    customer.setAuditType(auditType);
                    customerDao.save(customer);
                }
            });
        }
    }

    public List<Map<String, Object>> filterCustomer(Long id, String name) {
        List<Map<String, Object>> list = customerNative.filterCustomer(name, id);
        return list;
    }

    @Transactional
    public void getCustomerByNameAndId(Long id, String name, Long replaceId) {
//        List<Customer> customers = customerDao.findAllByNameAndIdIsNot(name, id);
//        if (customers.size() > 0) {
//            throw new WebException(400, "该客户名称已存在", null);
//        }
        Customer customer = customerDao.findById(id).get();
        if (replaceId != null) {
            // 存在同名时，先将id 对应的部门转移到replaceId ，再将工作经历中的id 都改为replaceId
            List<Department> departments = departmentDao.findAllByCustomerId(id);
            departments.forEach(item -> {
                item.setCustomerId(replaceId);
            });
            departmentDao.saveAll(departments);
            if (customer != null) {
                List<Experience> experiences = experienceDao.findAllByCustomerId(id);
                experiences.forEach(item -> {
                    item.setCustomerId(replaceId);
                });
                experienceDao.saveAll(experiences);
                customerDao.delete(customer);
            }
        } else {
            if (customer != null) {
                customer.setName(name);
                customer.setAuditType(2);
                customerDao.save(customer);
            }
        }
    }

}
