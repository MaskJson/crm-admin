package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ExperienceDao extends JpaRepository<Experience, Long>, JpaSpecificationExecutor<Experience> {

    List<Experience> findAllByTalentId(Long talentId);

    void removeAllByTalentId(Long talentId);

}
