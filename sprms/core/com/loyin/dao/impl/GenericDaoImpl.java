package com.jyd.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.jyd.dao.GenericDao;
import com.jyd.exception.BizException;
import com.jyd.po.BaseEntity;
import com.jyd.util.BeanUtils;
import com.jyd.util.Page;

public abstract class GenericDaoImpl<E extends BaseEntity> extends HibernateDaoSupport
		implements GenericDao<E> {
	protected Class<E> entityClass;// DAO所管理的Entity类

	protected static final Log log = LogFactory.getLog(GenericDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		super();
		this.entityClass = getSuperClassGenricType(getClass());
	}

	@SuppressWarnings("unchecked")
	protected Class getSuperClassGenricType(Class clazz) {
		Type genType = clazz.getGenericSuperclass();
		// if (!(genType instanceof ParameterizedType)) {
		// log.warn(clazz.getSimpleName()
		// + "'s superclass not ParameterizedType");
		// return Object.class;
		// }

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		// if (0 >= params.length) {
		// log.warn("Index: 0, Size of " + clazz.getSimpleName()
		// + "'s Parameterized Type: " + params.length);
		// return Object.class;
		// }
		// if (!(params[0] instanceof Class)) {
		// log.warn(clazz.getSimpleName()
		// + " not set the actual class on superclass generic parameter");
		// return Object.class;
		// }
		return (Class) params[0];
	}

	public boolean create(E entity) {
		try {
			this.getHibernateTemplate().save(entity);
			return true;
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			return false;
		}
	}

	public boolean evict(E entity) {
		try {
			this.getHibernateTemplate().evict(entity);
			return true;
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List queryByHql(String hql,Map<String,Object>params, int... maxresult) {
		Query query = getSession().createQuery(hql);
		setParam(query,params);
		if (maxresult != null && maxresult.length == 1) {
			query.setMaxResults(maxresult[0]);// 设置返回最大行数
		}
		return query.list();
	}

	public int execHql(String hql,Map<String,Object>params) {
		try {
			Query query = getSession().createQuery(hql);
			setParam(query,params);
			return query.executeUpdate();
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<E> findByFilter(Map<String, Object> filter, int... maxresult) {
		Criteria criteria = this.getCriteria();
		this.criteriaFilter(criteria, filter, maxresult);
		// return (List<E>)getHibernateTemplate().execute(
		// new HibernateCallback(){
		// public List<E> doInHibernate(Session session){
		//				    	  
		// return criteria.list();
		// }
		// }
		// ) ;
		// Hibernate.initialize(criteria);
//		List<E> list = criteria.list();
	//	list.size();// 防止懒加载
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public E get(Serializable id) {
		return (E) this.get(this.entityClass, id);
		// return (E) this.getHibernateTemplate().load(entityClass, id);
	}

	public int getTotal(Map<String, Object> filter) {
		Criteria criteria = this.getCriteria();
		criteriaFilter(criteria, filter);
		CriteriaImpl criteriaimpl=(CriteriaImpl)criteria;
		Projection pj=criteriaimpl.getProjection();
		criteria.setProjection(null);
		int totalCount = ((Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		criteria.setProjection(pj);
		return totalCount;
	}

	public Page pageByFilter(Map<String, Object> filter, int pageNo,
			int pageSize) {
		Criteria criteria = this.getCriteria();
		criteriaFilter(criteria, filter);
		return this.pagedQuery(criteria, pageNo, pageSize);
	}
	public void delFlag(Long id) {
		E entity = this.get(id);
		entity.setDelFlag(true);
		this.save(entity);
	}
	public boolean delete(E entity) {
		try {
			this.getHibernateTemplate().delete(entity);
			return true;
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			return false;
		}
	}

	public boolean removeById(Serializable id) {
		return this.delete(this.get(id));
	}

	public boolean save(E entity) {
		try {
//			if (entity.getId() != null && !"".equals(entity.getId())) {
				this.getSession().saveOrUpdate(entity);
//				this.getHibernateTemplate().saveOrUpdate(entity);
//			} else {
//				this.getHibernateTemplate().save(entity);
//			}
			return true;
		} catch (Exception ex) {
//			try{
//				this.getHibernateTemplate().save(entity);
//			}catch(Exception e){
//				log.error("save异常:",e);
//			}
			throw new BizException("saveOrUpdate异常",ex);
		}
	}

	/**
	 * <pre>
	 * 作用针对key为&quot;属性_关系&quot;的查询条件
	 * 比如 filter.put(&quot;age_le&quot;,20);年龄小于等于20 
	 * QBC
	 * </pre>
	 * 
	 * @param criteria
	 * @param filter
	 * @param int... maxresult
	 */
	protected void criteriaFilter(Criteria criteria,
			Map<String, Object> filter, int... maxresult) {
		try {
			Hibernate.initialize(criteria);
			if (null != maxresult && maxresult.length == 1) {
				criteria.setMaxResults(maxresult[0]);
			}
			if (filter != null) {
				for (String key : filter.keySet()) {
					Object value = filter.get(key);
					if (key != null && !"".equals(key)) {
						// 先判断等于的情况 如果没有下划线说明是默认等于运算
						if (!key.contains("_")) {
							criteria.add(Restrictions.eq(key, value));
							continue;
						}
					} else {
						continue;
					}
					String[] keys = key.split("_");
					keys[1] = keys[1].toLowerCase();

					// 对类取别名 filter.put("CLASS_alias","class");
					if ("alias".equals(keys[1])) {
						criteria.createAlias(keys[0], (String) value);
					} else
					// 不等于 !=
					if ("ne".equals(keys[1])) {
						criteria.add(Restrictions.ne(keys[0], value));
					} else
					// >
					if ("gt".equals(keys[1])) {
						criteria.add(Restrictions.gt(keys[0], value));
					} else
					// <
					if ("lt".equals(keys[1])) {
						criteria.add(Restrictions.lt(keys[0], value));
					} else
					// <=
					if ("le".equals(keys[1])) {
						criteria.add(Restrictions.le(keys[0], value));
					} else
					// >=
					if ("ge".equals(keys[1])) {
						criteria.add(Restrictions.ge(keys[0], value));
					} else if ("null".equals(keys[1])) {
						criteria.add(Restrictions.isNull(keys[0]));
					} else if ("notnull".equals(keys[1])) {
						criteria.add(Restrictions.isNotNull(keys[0]));
					} else if ("like".equals(keys[1])) {
						criteria.add(Restrictions.like(keys[0], value));
					} else if ("notlike".equals(keys[1])) {
						criteria.add(Restrictions.not(Restrictions.like(keys[0], value)));
					} else if ("in".equals(keys[1])) {
						criteria.add(Restrictions.in(keys[0], (Object[]) value));
					} else if ("notin".equals(keys[1])) {
						criteria.add(Restrictions.not(Restrictions.in(keys[0], (Object[]) value)));
					}// 还需优化 filter.put("col1 OR col2_or",{1,3}); --> col1=1 or col2=3
					else if ("or".equals(keys[1])) {
							String[] key1=keys[0].split(" OR ");
							if(key1.length==2){
								Object[] val=(Object[])value;
								criteria.add( Restrictions.or(Restrictions
										.eq(key1[0],val[0]), Restrictions.eq(
												key1[1], val[1])));
							}else{
								Object values = (Object) value;
								Criterion c = Restrictions.or(Restrictions
										.isNotNull(keys[0]), Restrictions.eq(
												keys[0], values));
									criteria.add(c);
							}
						/*if (value instanceof List) {
							List<Object[]> list = (List<Object[]>) value;
							Object[] values = list.get(0);
							Criterion c = null;
							if (values.length == 1) {
								c = Restrictions.or(Restrictions
										.isNotNull(keys[0]), Restrictions.eq(
										keys[0], values[0]));
							} else if (values.length > 1) {
								c=Restrictions.eq(keys[0], values[0]);
								for (int i = 1; i < values.length; i++) {
									c = Restrictions.or(c, Restrictions.eq(
											keys[0], values[i]));
								}
							}
							criteria.add(c);
						} else {
							Object values = (Object) value;
							Criterion c = Restrictions.or(Restrictions
									.isNotNull(keys[0]), Restrictions.eq(
											keys[0], values));
								criteria.add(c);
						}*/
					}
					else if ("order".equals(keys[1])) {
						if ("asc".equals(value)) {
							criteria.addOrder(Order.asc(keys[0]));
						} else {
							criteria.addOrder(Order.desc(keys[0]));
						}
					}
					// 分组
					else if ("group".equals(keys[1])) {
						criteria.setProjection(Projections.groupProperty(keys[0]));
					} else if ("fetch".equals(keys[0])) {
						criteria.setFetchSize((Integer) value);
					}
				}
			}
			// 唯一查询
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			// 设置缓存
			criteria.setCacheable(true);
		} catch (Exception ex) {
			throw new BizException(ex);
		}
	}
	@SuppressWarnings("unchecked")
	public Object get(Class entityclazz, final Serializable id) {
		return (E) getHibernateTemplate().execute(new HibernateCallback() {
			public E doInHibernate(Session session) {
				E entity = (E) session.load(entityClass, id);
				Hibernate.initialize(entity);
				return entity;
			}
		});
		// getHibernateTemplate().load(entityclazz, id);
	}

	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize) {
		CriteriaImpl impl = (CriteriaImpl) criteria;
//		CriteriaImpl impl2 = (CriteriaImpl) criteria;
		// 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
		Projection projection = impl.getProjection();
		List orderEntries;
		int totalCount = 0;
			orderEntries = (List) BeanUtils.forceGetProperty(impl,"orderEntries");
			BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
			Object count = impl.setProjection(Projections.rowCount()).uniqueResult();
			if (count != null) {
				totalCount = (Integer) count;
			}
			if (totalCount < 1)
				return new Page(0, pageNo, pageSize, null);
			// 将之前的Projection和OrderBy条件重新设回去
			if(orderEntries!=null&&!orderEntries.isEmpty())
			BeanUtils.forceSetProperty(impl, "orderEntries",orderEntries);
		// 将之前的Projection和OrderBy条件重新设回去
		criteria.setProjection(projection);
		if (projection == null) {
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		}
		// 返回分页对象
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		List list = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();
		return new Page(totalCount, pageNo, pageSize, list);
	}

	protected Criteria getCriteria() {
		return getSession().createCriteria(this.entityClass);
	}

	public boolean isUnique(E entity, String uniquePropertyNames) {
		Criteria criteria = createCriteria(entityClass).setProjection(
				Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (String name : nameList) {
				criteria.add(Restrictions.eq(name, PropertyUtils.getProperty(
						entity, name)));
			}
			// 以下代码为了如果是update的情况,排除entity自身.
			String idName = getIdName();
			// 取得entity的主键值
			Serializable id = getId(entity);
			// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
			if (id != null)
				criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
		return (Integer) criteria.uniqueResult() == 0;
	}

	public Serializable getId(Object entity) {
		Assert.notNull(entity);
		try {
			return (Serializable) PropertyUtils
					.getProperty(entity, getIdName());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return null;
		}
	}

	protected String getIdName() {
		ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 创建Criteria对象
	 */
	@SuppressWarnings("hiding")
	protected <E> Criteria createCriteria(Class<E> entity) {
		Criteria criteria = getSession().createCriteria(entity);
		return criteria;
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

	public void clear() {
		getHibernateTemplate().clear();
	}

	public Query createQuery(String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	public int execSql(String sql,int...maxResult ) {
			SQLQuery query=this.getSession().createSQLQuery(sql);
			if(maxResult!=null&& maxResult.length == 1)
			query.setMaxResults(maxResult[0]);
			return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List execSqlQuery(String sql, int... maxresult) {
		try {
			SQLQuery query = this.getSession().createSQLQuery(sql);
			if (null != maxresult && maxresult.length == 1) {
				query.setMaxResults(maxresult[0]);
			}
			return query.list();
		} catch (Exception ex) {
			throw new BizException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<E> findByExample(E entity) {
		try {
			List<E> results = getHibernateTemplate().findByExample(entity);
			return results;
		} catch (Exception re) {
			throw new BizException("find by example failed!:",re);
		}
	}

	@SuppressWarnings("unchecked")
	public E merge(E entity) {
		log.debug("merging Test instance");
		try {
			return (E) getHibernateTemplate().merge(entity);
		} catch (RuntimeException re) {
			throw new BizException("merge failed",re);
		}
	}

	@SuppressWarnings("unchecked")
	public E queryUnique(Map<String, Object> filter) {
		Criteria criteria = this.getCriteria();
		this.criteriaFilter(criteria, filter);
		return (E) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public E queryUnique(String hql,Map<String,Object>params) {
		Query query = getSession().createQuery(hql);
		setParam(query,params);
		return (E)query.uniqueResult();
	}

	public Object queryUniqueObject(Map<String, Object> filter) {
		Criteria criteria = this.getCriteria();
		this.criteriaFilter(criteria, filter);
		return criteria.uniqueResult();
	}

	public Object queryUniqueObject(String hql,Map<String,Object>params) {
		Query query = getSession().createQuery(hql);
		setParam(query,params);
		return query.uniqueResult();
	}

	public Page pageByHsql(String hql, int pageNo, int pageSize, Map<String, Object>... params) {
		String counthql = "select count(*) " + hql;
		Query query = getSession().createQuery(counthql);
		Long totalCount = (Long) query.uniqueResult();
		if(params!=null)
			setParam(query,params[0]);
		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		query = getSession().createQuery(hql);
		query.setMaxResults(pageSize);
		if(params!=null)
			setParam(query,params[0]);
		query.setFirstResult(startIndex);
		return new Page(totalCount, pageNo, pageSize, query.list());
	}

	@SuppressWarnings("unchecked")
	public List<E> nameQuery(String queryname, Map<String, Object> param) {
		Query query = this.getSession().getNamedQuery(queryname);
		setParam(query,param);
		return query.list();
	}
	@SuppressWarnings("unchecked")
	protected void setParam(Query query, Map<String, Object> param){
		if(param!=null&&!param.isEmpty())
		for (String key : param.keySet()) {
			if (key.endsWith("_array")) {
				query.setParameterList(key.replace("_array", ""),(List)param.get(key));
			} else {
				query.setParameter(key, param.get(key));
			}
		}
	}
	@SuppressWarnings("unchecked")
	public Page pageBySQL(String sql,int pageNo,int pageSize){
		int i=sql.lastIndexOf("order");
		String countsql="select count(*) from ("+(i>0?sql.substring(0,i ):sql)+") ddjflkds";
		int count=Integer.parseInt( this.execSqlQuery(countsql, 1).get(0).toString());
		if(count<=0){
			return new Page(0,pageNo,pageSize,null);
		}
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setReadOnly(true);
		query.setMaxResults(pageSize);
		query.setFirstResult((pageNo-1)*pageSize);
		List list=query.list();
		return new Page(count,pageNo,pageSize,list);
	}
	public Page pageBySQL2(String countSql, String sql, int pageNo, int pageSize) {
		int count=Integer.parseInt( this.execSqlQuery(countSql, 1).get(0).toString());
		if(count<=0){
			return new Page(0,pageNo,pageSize,null);
		}
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setReadOnly(true);
		query.setMaxResults(pageSize);
		query.setFirstResult((pageNo-1)*pageSize);
		List list=query.list();
		return new Page(count,pageNo,pageSize,list);
	}

	public List execSqlQuery(String sql, List<Object> paramlist) {
		Query query = getSession().createSQLQuery(sql);
		int i=0;;
		if(paramlist!=null&&paramlist.isEmpty()==false)
		for(Object val:paramlist){
			query.setParameter(i++, val);
		}
		return query.list();
	}
}

