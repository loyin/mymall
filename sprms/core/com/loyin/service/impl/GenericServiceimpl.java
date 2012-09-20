package com.jyd.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;

import com.jyd.dao.GenericDao;
import com.jyd.service.GenericService;
import com.jyd.util.Page;

public class GenericServiceimpl<E, Dao extends GenericDao<E>>
		implements GenericService<E> {
	protected Dao dao;

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public Dao getDao() {
		return dao;
	}

	public void clear() {
		dao.clear();
	}

	public boolean create(E entity) {

		return dao.create(entity);
	}

	public Query createQuery(String hql, Object... values) {

		return dao.createQuery(hql, values);
	}

	public boolean delete(E entity) {

		return dao.delete(entity);
	}

	public boolean evict(E entity) {

		return dao.evict(entity);
	}

	public int execHql(String hql, Map<String, Object> params) {

		return dao.execHql(hql, params);
	}

	@SuppressWarnings("unchecked")
	public List<E> queryByHql(String hql, Map<String, Object> params,
			int... maxresult) {

		return dao.queryByHql(hql, params, maxresult);
	}

	public int execSql(String sql,int...maxresult) {

		return dao.execSql(sql,maxresult);
	}

	public List execSqlQuery(String sql, List<Object> paramlist) {
		return dao.execSqlQuery(sql,paramlist);
	}

	public List<E> findByExample(E entity) {

		return dao.findByExample(entity);
	}

	public List<E> findByFilter(Map<String, Object> filter, int... maxresult) {

		return dao.findByFilter(filter,maxresult);
	}

	public void flush() {
		dao.flush();

	}

	public E get(Serializable id) {

		return dao.get(id);
	}

	public Object get(Class entityclazz, Serializable id) {

		return dao.get(entityclazz, id);
	}

	public int getTotal(Map<String, Object> zjFilter) {

		return dao.getTotal(zjFilter);
	}

	public boolean isUnique(E entity, String uniquePropertyNames) {

		return dao.isUnique(entity, uniquePropertyNames);
	}

	public E merge(E entity) {

		return dao.merge(entity);
	}

	public Page pageByFilter(Map<String, Object> filter, int pageNo,
			int pageSize) {

		return dao.pageByFilter(filter, pageNo, pageSize);
	}

	public Page pageByHsql(String hql, int pageNo, int pageSize,
			Map<String, Object>... params) {

		return dao.pageByHsql(hql, pageNo, pageSize, params);
	}

	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {

		return dao.pagedQuery(criteria, pageNo, pageSize);
	}

	public E queryUnique(Map<String, Object> filter) {

		return dao.queryUnique(filter);
	}

	public E queryUnique(String hql, Map<String, Object> params) {

		return dao.queryUnique(hql, params);
	}

	public Object queryUniqueObject(Map<String, Object> filter) {

		return dao.queryUniqueObject(filter);
	}

	public Object queryUniqueObject(String hql, Map<String, Object> params) {

		return dao.queryUniqueObject(hql, params);
	}

	public boolean removeById(Serializable id) {

		return dao.removeById(id);
	}

	public boolean save(E entity) {

		return dao.save(entity);
	}

	public List<E> nameQuery(String queryname, Map<String, Object> param) {
		return dao.nameQuery(queryname, param);
	}

	public Page pageBySQL(String sql, int pageNo, int pageSize) {
		return this.pageBySQL(sql, pageNo, pageSize);
	}

	public Page pageBySQL2(String countSql, String sql, int pageNo, int pageSize) {
		return this.dao.pageBySQL2(countSql, sql, pageNo, pageSize);
	}

	public void delFlag(Long id) {
		this.dao.delFlag(id);
	}

	public List execSqlQuery(String sql, int... maxresult) {
		return this.dao.execSqlQuery(sql, maxresult);
	}
}
