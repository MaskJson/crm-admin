package com.moving.admin.dao.sys;

import com.moving.admin.entity.sys.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamDao extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    List<Team> findAllByTeamId(Long teamId);

    void deleteAllByTeamId(Long teamId);

    Team findTeamByUserIdAndLevel(Long userId, Integer level);

    @Query(value = "select count(id) from Team where teamId=:teamId")
    Long getCountOfTeam(Long teamId);

}
