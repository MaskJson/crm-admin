package com.moving.admin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.moving.admin.dao.natives.TeamNative;
import com.moving.admin.dao.sys.TeamDao;
import com.moving.admin.dao.sys.UserDao;
import com.moving.admin.entity.sys.Team;
import com.moving.admin.entity.sys.User;
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
        List<Long> mPls = team.getMpls();
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
            mPls.forEach(id -> {
                teams.add(getTeamItem(id, null, teamId, 4));
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
}


