package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TalentDao extends JpaRepository<Talent, Long>, JpaSpecificationExecutor<Talent> {

    Talent findTalentByPhone(String phone);

}
