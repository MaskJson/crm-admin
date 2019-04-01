package com.moving.admin.dao.talent;

import com.moving.admin.entity.talent.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FriendDao extends JpaRepository<Friend, Long>, JpaSpecificationExecutor<Friend> {

    List<Friend> findAllByTalentId(Long talentId);

    void removeAllByTalentId(Long talentId);

}
