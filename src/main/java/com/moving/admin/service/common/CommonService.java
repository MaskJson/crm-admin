package com.moving.admin.service.common;

import com.moving.admin.dao.common.AptnessDao;
import com.moving.admin.dao.common.IndustryDao;
import com.moving.admin.entity.common.Aptness;
import com.moving.admin.entity.common.Industry;
import com.moving.admin.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService extends AbstractService {

    @Autowired
    private IndustryDao industryDao;

    @Autowired
    private AptnessDao aptnessDao;

    // 获取所有行业
    public List<Industry> getAllIndustry() {
        List<Industry> industryList = industryDao.findAll();
        List<Industry> list = new ArrayList<>();
        List<Industry> subList = new ArrayList<>();
        for (Industry industry : industryList) {
            Long parentId = industry.getParentId();
            if (parentId == 0 || parentId == null) {
                list.add(industry);
            } else {
                subList.add(industry);
            }
        }
        for (Industry industry : list) {
            List<Industry> children = new ArrayList<>();
            for (Industry child : subList) {
                if (industry.getId() == child.getParentId()) {
                    children.add(child);
                }
            }
            industry.setIndustryList(children);
        }
        return list;
    }

    // 获取所有职能
    public List<Aptness> getAllAptness() {
        List<Aptness> aptnessList = aptnessDao.findAll();
        List<Aptness> list = new ArrayList<>();
        List<Aptness> subList = new ArrayList<>();
        for (Aptness aptness : aptnessList) {
            Long parentId = aptness.getParentId();
            if (parentId == 0 || parentId == null) {
                list.add(aptness);
            } else {
                subList.add(aptness);
            }
        }
        for (Aptness aptness : list) {
            List<Aptness> children = new ArrayList<>();
            for (Aptness child : subList) {
                if (aptness.getId() == child.getParentId()) {
                    children.add(child);
                }
            }
            aptness.setAptnessList(children);
        }
        return list;
    }
}
