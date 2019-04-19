package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TalentDao extends JpaRepository<Talent, Long>, JpaSpecificationExecutor<Talent> {

    Talent findTalentByPhone(String phone);

    // 获取所有专属人才
    List<Talent> findAllByType(Integer type);

    List<Talent> findAllByCreateUserIdOrFollowUserId(Long createUserId, Long followUserId);

}
