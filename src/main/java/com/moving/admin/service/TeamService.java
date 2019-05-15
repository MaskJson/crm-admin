package com.moving.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.moving.admin.dao.customer.CustomerContactDao;
import com.moving.admin.dao.customer.CustomerContactRemarkDao;
import com.moving.admin.dao.customer.CustomerDao;
import com.moving.admin.dao.customer.CustomerRemindDao;
import com.moving.admin.dao.natives.TeamNative;
import com.moving.admin.dao.project.ProjectDao;
import com.moving.admin.dao.project.ProjectRemindDao;
import com.moving.admin.dao.project.ProjectReportDao;
import com.moving.admin.dao.project.ProjectTalentDao;
import com.moving.admin.dao.sys.TeamDao;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.dao.talent.TalentDao;
import com.moving.admin.dao.talent.TalentRemindDao;
import com.moving.admin.entity.customer.Customer;
import com.moving.admin.entity.customer.CustomerContact;
import com.moving.admin.entity.customer.CustomerContactRemark;
import com.moving.admin.entity.customer.CustomerRemind;
import com.moving.admin.entity.project.Project;
import com.moving.admin.entity.project.ProjectRemind;
import com.moving.admin.entity.project.ProjectReport;
import com.moving.admin.entity.project.ProjectTalent;
import com.moving.admin.entity.sys.Team;
import com.moving.admin.entity.sys.User;
import com.moving.admin.entity.talent.Talent;
import com.moving.admin.entity.talent.TalentRemind;
import com.moving.admin.exception.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TeamService extends AbstractService {

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TeamNative teamNative;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectTalentDao projectTalentDao;

    @Autowired
    private ProjectRemindDao projectRemindDao;

    @Autowired
    private ProjectReportDao projectReportDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemindDao customerRemindDao;

    @Autowired
    private CustomerContactDao customerContactDao;

    @Autowired
    private CustomerContactRemarkDao customerContactRemarkDao;

    @Autowired
    private TalentDao talentDao;

    @Autowired
    private TalentRemindDao talentRemindDao;

    /**
     * // 添加-编辑团队
     * @param team
     * @return
     */
    @Transactional
    public Long save(Team team) {
        // 添加操作，设置level
        if (team.getId() == null) {
            team.setLevel(1);
            teamDao.save(team);
        } else {
            // 编辑
            teamDao.deleteAllByTeamId(team.getId());
        }
        Long teamId = team.getId();
        List<Map<String, Object>> pms = team.getPms();
        List<Map<String, Object>> iPls = team.getIpls();
        List<Map<String, Object>> mPls = team.getMpls();
        List<Long> pts = team.getPts();
        // 项目经理
        List<Team> teams = new ArrayList<>();
        if (pms != null) {
            pms.forEach(pm -> {
                setTeamList(teamId, teams, pm, 2);
            });
        }
        // 高级顾问
        if (iPls != null) {
            iPls.forEach(ipl -> {
                setTeamList(teamId, teams, ipl, 3);
            });
        }
        //中级顾问
        if (mPls != null) {
            iPls.forEach(ipl -> {
                setTeamList(teamId, teams, ipl, 4);
            });
        }
        // 兼职
        if (pts != null) {
            pts.forEach(id -> {
                teams.add(getTeamItem(id, null, teamId, 5));
            });
        }
        List<Team> list = teamDao.saveAll(teams);
        return teamId;
    }

    public void setTeamList(Long teamId, List<Team> teams, Map<String, Object> map, Integer level) {
        Team t = getTeamItem(Long.parseLong(map.get("userId").toString()), null, teamId, level);
        teamDao.save(t);
        Long parentId = t.getId();
        JSONArray array = JSON.parseArray(map.get("children").toString());
        array.forEach(id -> {
            teams.add(getTeamItem(Long.parseLong(id.toString()), parentId, teamId, null));
        });
    }

    /**
     * 获取团队成员
     * @param teamId
     * @return
     */
    public List<Team> getTeamMembersByTeamId(Long teamId) {
        return teamDao.findAllByTeamId(teamId);
    }

    // 根据总监id获取团队成员
    public List<Team> getMembersByUserId(Long userId) {
        Team team = teamDao.findTeamByUserIdAndLevel(userId, 1);
        if (team != null) {
            return getTeamMembersByTeamId(team.getId());
        } else {
            return null;
        }
    }

    /**
     * 获取团队成员信息
     */
    public List<Map<String, Object>> getTeamMembersWithInfo(Long teamId) {
        return teamNative.getTeamMemberWithInfo(teamId);
    }

    /**
     * 获取团队列表
     * @param name
     * @param pageable
     * @return
     */
    public Page<User> getTeamList(String name, Pageable pageable) {
        Page<User> result = userDao.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(name)) {
                list.add(cb.like(root.get("nickName"), "%" + name + "%"));
            }
            list.add(cb.equal(root.get("roleId"), 3));
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
        result.forEach(user -> {
            Team team = teamDao.findTeamByUserIdAndLevel(user.getId(), 1);
            if (team != null) {
                user.setTeamId(team.getId());
                user.setCount(teamDao.getCountOfTeam(team.getId()));
            }
        });
        return result;
    }

    public List<Map<String, Object>> getTeamManagerUsers() {
        return teamNative.getTeamManagerUsers();
    }

    /**
     * 获取Team实例
     * @param userId
     * @param parentId
     * @param teamId
     * @param level
     * @return
     */
    public Team getTeamItem(Long userId, Long parentId, Long teamId, Integer level) {
        Team t = new Team();
        t.setUserId(userId);
        t.setParentId(parentId);
        t.setTeamId(teamId);
        t.setLevel(level);
        return t;
    }

    /**
     * 团队交接(总监离职）
     * 原团队成员全部转为交接的团队成员，成员等级不变
     * 该用户所有创建的项目，创建人id 改为交接的总监id
     */
    public void teamConnect(Long teamId, Long connectTeamId, Long userId, Long connectUserId) {
        Team team = teamDao.findById(teamId).get();
        Team connectTeam = teamDao.findById(connectTeamId).get();
        if (team != null && connectTeam != null && team.getLevel() == 1 && connectTeam.getLevel() == 1 && team.getUserId() == userId && connectTeam.getUserId() == connectUserId) {
            // 所有相关项目 createUserId 和 teamId
            List<Project> projects = projectDao.findAllByCreateUserId(userId);
            projects.forEach(project -> {
                project.setCreateUserId(connectUserId);
                project.setTeamId(connectTeamId);
            });
            projectDao.saveAll(projects);
            ////// 所有相关客户的createUserId 和 followUserId
            List<Customer> customers = customerDao.findAllByCreateUserIdOrFollowUserId(userId, userId);
            customers.forEach(customer -> {
                if (customer.getCreateUserId() == userId) {
                    customer.setCreateUserId(connectUserId);
                }
                if (customer.getFollowUserId() == userId) {
                    customer.setFollowUserId(connectUserId);
                }
            });
            customerDao.saveAll(customers);
            List<CustomerContact> customerContacts = customerContactDao.findAllByCreateUserId(userId);
            customerContacts.forEach(customerContact -> {
                customerContact.setCreateUserId(connectUserId);
            });
            customerContactDao.saveAll(customerContacts);
            List<CustomerContactRemark> customerContactRemarks = customerContactRemarkDao.findAllByCreateUserId(userId);
            customerContactRemarks.forEach(customerContactRemark -> {
                customerContactRemark.setCreateUserId(connectUserId);
            });
            customerContactRemarkDao.saveAll(customerContactRemarks);
            // 团队成员转移
            List<Team> teams = teamDao.findAllByTeamId(teamId);
            teams.forEach(t -> {
                t.setTeamId(connectTeamId);
            });
            teamDao.saveAll(teams);
            actionConnect(userId, connectUserId);
            // 删除离职的用户及团队记录
            teamDao.delete(team);
            userDao.deleteById(userId);
        } else {
            throw new WebException(400, "数据有误，请刷新页面", null);
        }
    }

    /**
     * 普通成员交接,若该用户是团队的成员，将所有相关userId 改为交接的用户 id
     */
    public void memberConnect(Long userId, Long connectUserId) {
        // 查找离职成员所在的团队
        List<Team> teams = teamDao.findAllByUserId(userId);
        teams.forEach(team -> {
            Team t = teamDao.findTeamByUserIdAndTeamId(connectUserId, team.getTeamId());
            if (t == null) {
                team.setUserId(connectUserId);
                teamDao.save(team);
            } else {
                teamDao.delete(team);
            }
        });
        actionConnect(userId, connectUserId);
        userDao.deleteById(userId);
    }

    /**
     * 所有相关操作createUserId ,改为交接的用户 id
     */
    public void actionConnect(Long userId, Long connectUserId) {
        // 人才相关
        List<Talent> talents = talentDao.findAllByCreateUserIdOrFollowUserId(userId, userId);
        talents.forEach(talent -> {
            if (talent.getCreateUserId() == userId) {
                talent.setCreateUserId(connectUserId);
            }
            if (talent.getFollowUserId() == userId) {
                talent.setFollowUserId(connectUserId);
            }
        });
//        List<TalentRemind> talentReminds = talentRemindDao.findAllByCreateUserId(userId);
//        talentReminds.forEach(talentRemind -> {
//            talentRemind.setCreateUserId(connectUserId);
//        });
//        talentRemindDao.saveAll(talentReminds);
        talentDao.saveAll(talents);
        // 项目进展人才相关
        List<ProjectTalent> projectTalents = projectTalentDao.findAllByCreateUserId(userId);
        projectTalents.forEach(projectTalent -> {
            projectTalent.setCreateUserId(connectUserId);
        });
        projectTalentDao.saveAll(projectTalents);
//        List<ProjectReport> projectReports = projectReportDao.findAllByCreateUserId(userId);
//        projectReports.forEach(projectReport -> {
//            projectReport.setCreateUserId(connectUserId);
//        });
//        projectReportDao.saveAll(projectReports);
//        List<ProjectRemind> projectReminds = projectRemindDao.findAllByCreateUserId(userId);
//        projectReminds.forEach(projectRemind -> {
//            projectRemind.setCreateUserId(connectUserId);
//        });
//        projectRemindDao.saveAll(projectReminds);
    }
}


