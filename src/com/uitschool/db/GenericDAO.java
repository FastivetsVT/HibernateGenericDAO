package com.uitschool.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDAO<Entity extends Serializable, ID extends Serializable> {

	boolean save(Entity obj);

	Entity getByID(ID id);

	Entity getByParameters(Map<String, Object> params);

	List<Entity> getList();

	List<Entity> getListByParameters(Map<String, Object> params);

	boolean delete(ID id);
}