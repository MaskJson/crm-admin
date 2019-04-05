package com.moving.admin.service;

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
        List<Map<String, Object>> iPls = team.getIPls();
        List<Long> mPls = team.getMPls();
        List<Long> pts = team.getPts();
        // 项目经理
        List<Team> teams = new ArrayList<>();
        pms.forEach(pm -> {
            Team t = getTeamItem((long)pm.get("userId"), null, teamId, 2);
            teamDao.save(t);
            Long parentId = t.getId();
            ((List<Long>)pm.get("children")).forEach(id -> {
                teams.add(getTeamItem(id, parentId, teamId, null));
            });
        });
        // 高级顾问
        iPls.forEach(ipl -> {
            Team t = getTeamItem((long)ipl.get("userId"), null, teamId, 3);
            teamDao.save(t);
            Long parentId = t.getId();
            ((List<Long>)ipl.get("Children")).forEach(id -> {
                teams.add(getTeamItem(id, parentId, teamId, null));
            });
        });
        // 中级顾问
        mPls.forEach(id -> {
            teams.add(getTeamItem(id, null, teamId, 4));
        });
        // 兼职
        pts.forEach(id -> {
            teams.add(getTeamItem(id, null, teamId, 5));
        });
        teamDao.saveAll(teams);
        return teamId;
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
            Predicate[] predicates = new Predicate[list.size()];
            return query.where(list.toArray(predicates)).getRestriction();
        }, pageable);
        result.forEach(user -> {
            Team team = teamDao.findTeamByUserIdAndLevel(user.getId(), 1);
            if (team != null) {
                user.setTeamId(team.getId());
            }
        });
        return result;
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


