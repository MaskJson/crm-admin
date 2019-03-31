package com.moving.admin.service;

import com.moving.admin.dao.talent.TalentDao;
import com.moving.admin.entity.talent.Talent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TalentService extends AbstractService {

    @Autowired
    private TalentDao talentDao;

    // 手机号验证重复
    public Talent checkPhone(String phone) {
        Talent talent = null;
        talent = talentDao.findTalentByPhone(phone);
        return talent;
    }
}
