package com.moving.admin.service;


import com.moving.admin.dao.natives.TestNative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moving.admin.dao.TestDao;
import com.moving.admin.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.Predicate;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private TestDao testDao;

	@PersistenceUnit
	public void setEneityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
	
	public Page<Order> getPage(Pageable pageable) {
		return testDao.findAll(pageable);
	}

	public Map<String, Object> getNativeList(TestNative testNative) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Map<String, Object>> list = testNative.getList(entityManager);
		BigInteger total = testNative.getTotal(entityManager);
		Map<String, Object> map = new HashMap<>();
		map.put("content", list);
		map.put("totalElements", total);
		return map;
	}

	// 单表带参分页查询 Predicate
	public Page<Order> getList(Pageable pageable, Long userId) {
		return testDao.findAll((root, query, cb) -> {
			List<Predicate> predicateList = new ArrayList<>();
			if (null != userId) {
				predicateList.add(cb.equal(root.get("userId").as(Long.class), userId)); // 添加查询条件
			}
			Predicate[] predicates = new Predicate[predicateList.size()];
			return query.where(predicateList.toArray(predicates)).getRestriction();
		}, pageable);
	}

	// 单表分页查询练习
	public Page<Order> getList2(Pageable pageable, Long userId) {
		return testDao.findAll((root, query, cb) -> {
			List<Predicate> pl = new ArrayList<>();
			if (null != userId) {
				pl.add(cb.equal(root.get("userId").as(String.class), userId));
			}
			Predicate[] pds = new Predicate[pl.size()];
			return query.where(pl.toArray(pds)).getRestriction();
		}, pageable);
	}
	
	public Order getDetail(Long id) {
		return testDao.findById(id).get();
	}

}
