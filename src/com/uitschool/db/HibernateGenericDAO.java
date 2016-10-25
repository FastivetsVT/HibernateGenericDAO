package com.uitschool.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class HibernateGenericDAO<Entity extends Serializable, ID extends Serializable>
		implements GenericDAO<Entity, ID> {

	private Class<Entity> entityClass;

	public static <E extends Serializable, I extends Serializable> HibernateGenericDAO<E, I> getInstanceDAO(
			Class<E> entityClass, Class<I> idClass) {
		if (!HibernateUtil.isMappedClass(entityClass)) {
			throw new IllegalArgumentException("Unmapped class");
		}
		if (!HibernateUtil.isValidClassID(entityClass, idClass)) {
			throw new IllegalArgumentException("Illegal primary key type");
		}
		return new HibernateGenericDAO<E, I>(entityClass, idClass);
	}

	private HibernateGenericDAO(Class<Entity> entityClass, Class<ID> c) {
		this.entityClass = entityClass;
	}

	@Override
	public boolean save(Entity obj) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
			transaction.commit();
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			return false;
		}
	}

	@Override
	public Entity getByID(ID id) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			return (Entity) session.createCriteria(entityClass).add(Restrictions.idEq(id)).uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		} finally {
			transaction.commit();
		}
	}

	@Override
	public Entity getByParameters(Map<String, Object> params) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			return (Entity) session.createCriteria(entityClass).add(Restrictions.allEq(params)).uniqueResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		} finally {
			transaction.commit();
		}
	}

	@Override
	public List<Entity> getList() {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			return session.createCriteria(entityClass).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		} finally {
			transaction.rollback();
		}
	}

	@Override
	public List<Entity> getListByParameters(Map<String, Object> params) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			return session.createCriteria(entityClass).add(Restrictions.allEq(params)).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		} finally {
			transaction.rollback();
		}
	}

	@Override
	public boolean delete(ID id) {
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			Object objToDelete = session.get(entityClass, id);
			if (objToDelete == null) {
				transaction.rollback();
				return false;
			}
			session.delete(objToDelete);
			transaction.commit();
			return true;
		} catch (HibernateException e) {
			e.printStackTrace();
			transaction.rollback();
			return false;
		}
	}
}