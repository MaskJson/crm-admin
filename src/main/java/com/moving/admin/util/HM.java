package com.moving.admin.util;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 快捷创建map以及添加元素
 * 
 * @author Administrator
 *
 */
public class HM {

	private Map<String, Object> map = Maps.newHashMap();

	private HM() {
	}

	public HM put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public static HM map() {
		return new HM();
	}

	public Map<String, Object> end() {
		return map;
	}
	
	public HM wrap(String key) {
		Map<String, Object> newMap = Maps.newHashMap();
		newMap.put(key, map);
		this.map = newMap;
		return this;
	}
}
