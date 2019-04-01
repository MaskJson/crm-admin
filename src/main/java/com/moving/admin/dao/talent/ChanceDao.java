package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.Chance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChanceDao extends JpaRepository<Chance, Long>, JpaSpecificationExecutor<Chance> {

    List<Chance> findAllByTalentId(Long talentId);

    void removeAllByTalentId(Long talentId);

}
