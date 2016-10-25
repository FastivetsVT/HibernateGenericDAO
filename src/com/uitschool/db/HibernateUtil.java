package com.uitschool.db;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Iterator;

public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	public static Configuration config;

	static {
		try {
			config = new Configuration().configure().setProperty("hibernate.current_session_context_class", "thread");
			sessionFactory = config.buildSessionFactory();
		} catch (HibernateException ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static boolean isMappedClass(Class<?> entityClass) {
		Iterator<PersistentClass> i = config.getClassMappings();
		while (i.hasNext()) {
			if (i.next().getMappedClass().equals(entityClass)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isValidClassID(Class<?> entityClass, Class<?> idClass) {
		return config.getClassMapping(entityClass.getName()).getIdentifier().getType().getReturnedClass()
				.equals(idClass);
	}

	public static Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}