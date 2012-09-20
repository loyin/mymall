package com.jyd.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;

import com.jyd.util.Page;

/**
 * 普通方法的DAO 简单CURD
 * 
 * @author loyin
 * 
 * @param <T>
 */
public interface GenericDao<E> {
	/**
	 * 通过ID查询
	 * 
	 * @param Serializable
	 *            id
	 * @return <E>
	 */
	public E get(Serializable id);

	/**
	 * 回收
	 * 
	 * @param entity
	 */
	public boolean evict(E entity);

	/**
	 * 创建
	 * 
	 * @param E
	 *            entity
	 */
	public boolean create(E entity);

	/**
	 * 保存
	 * 
	 * @param E
	 *            entity
	 */
	public boolean save(E entity);
	/**
	 * 通过id设置成删除状态
	 * 
	 * @param id
	 */
	public void delFlag(Long id);
	/**
	 * 物理删除
	 * 
	 * @param E
	 *            entity
	 */
	public boolean delete(E entity);

	/**
	 * 执行没有返回结果集的HQL<br>
	 * 如update、delete等语句
	 * 
	 * @param hql
	 * @return int
	 */
	public int execHql(String hql, Map<String, Object> params);

	/**
	 * 执行没有返回结果集的SQL<br>
	 * 如update、delete等语句
	 * 
	 * @param sql
	 * @param int... maxResult返回的数据集大小
	 * @return int
	 */
	public int execSql(String sql,int...maxresult);

	/**
	 * 通过HQL查询
	 * 
	 * @param hql
	 * @param params
	 * @param int... maxresult
	 * @return List&lt;E&gt;
	 */
	public List queryByHql(String hql, Map<String, Object> params,
			int... maxresult);

	/**
	 * 通过SQL查询
	 * 
	 * @param sql
	 * @return List
	 */
	public List execSqlQuery(String sql, int... maxresult);
	/**
	 * 通过SQL查询
	 * 
	 * @param sql
	 * @return List
	 */
	public List execSqlQuery(String sql,List<Object> paramlist);

	/**
	 * 通过ID删除
	 * 
	 * @param id
	 */
	public boolean removeById(Serializable id);

	/**
	 * QBC 通过Map filter 查询
	 * 
	 * @param Map
	 *            &lt;String,Object&gt;filter
	 * @return List&lt;E&gt;
	 */
	public List<E> findByFilter(Map<String, Object> filter, int... maxresult);

	/**
	 * 通过示例查询 QBE
	 * 
	 * @param entity
	 * @return List&lt;E&gt;
	 */
	public List<E> findByExample(E entity);

	/**
	 * 合并
	 * 
	 * @param E
	 *            entity
	 * @return E
	 */
	public E merge(E entity);

	/**
	 * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
	 */
	@SuppressWarnings("unchecked")
	public Object get(Class entityclazz, final Serializable id);

	/**
	 * 获得总数
	 * 
	 * @param Map
	 *            &lt;String,Object&gt; filter
	 * @return int
	 */
	public int getTotal(Map<String, Object> zjFilter);

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(E entity, String uniquePropertyNames);

	/**
	 * 刷新缓存
	 */
	public void flush();

	/**
	 * 清楚缓存
	 */
	public void clear();


	/**
	 * <pre>
	 * 创建Query对象. 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
	 * 留意可以连续设置,如下：
	 * String hql=&quot;from entity e where id=?,name=?&quot;;&lt;br&gt;
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * 调用方式如下：
	 *        dao.createQuery(hql)
	 *        dao.createQuery(hql,arg0);
	 *        dao.createQuery(hql,arg0,arg1);
	 *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
	 * </pre>
	 * 
	 * @param values
	 *            可变参数.
	 */
	public Query createQuery(String hql, Object... values);

	/**
	 * criteria单一查询查询
	 * 
	 * @param Map
	 *            &lt;String,Object&gt;filter
	 * @return E
	 */
	public E queryUnique(Map<String, Object> filter);

	/**
	 * Query单一查询查询
	 * 
	 * @param String
	 *            hql
	 * @return E
	 */
	public E queryUnique(String hql, Map<String, Object> params);

	/**
	 * criteria单一查询查询
	 * 
	 * @param Map
	 *            &lt;String,Object&gt; filter
	 * @return Object
	 */
	public Object queryUniqueObject(Map<String, Object> filter);

	/**
	 * 命名查询
	 * 
	 * @param queryname
	 * @param Map
	 *            &lt;String ,Object&gt; param
	 * 
	 *            <pre>
	 * param的数值如下示例：
	 * key		value
	 * filde_array  {&quot;12&quot;,&quot;32&quot;} -- 是对于数组参数
	 * filde1		&quot;name&quot;
	 * filde2		123
	 * </pre>
	 * @return List&lt;E&gt;
	 */
	public List<E> nameQuery(String queryname, Map<String, Object> param);

	/**
	 * Query单一查询查询
	 * 
	 * @param filter
	 * @return
	 */
	public Object queryUniqueObject(String hql, Map<String, Object> param);

	/**
	 * 分页查询函数，使用已设好查询条件与排序的<code>Criteria</code>.
	 * 
	 * @param pageNo
	 *            页号,从1开始.
	 * @return 含总记录数和当前页数据的Page对象.
	 */
	public Page pagedQuery(Criteria criteria, int pageNo, int pageSize);

	/**
	 * QBC 分页查询<br>
	 * 
	 * @param Map
	 *            <String, Object> filter
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	public Page pageByFilter(Map<String, Object> filter, int pageNo,
			int pageSize);

	/**
	 * 分页查询<br>
	 * 
	 * @param String
	 *            hql
	 * @param pageNo
	 * @param pageSize
	 * @return Page
	 */
	public Page pageByHsql(String hql, int pageNo, int pageSize,
			Map<String, Object>... params);
	/***
	 * SQL分页查询
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page pageBySQL(String sql,int pageNo,int pageSize);
	/**
	 * 针对复杂SQL分页查询
	 * @param countSql 总数统计
	 * @param sql      主SQL
	 * @param pageNo   显示页码
	 * @param pageSize 每页显示数量
	 * @return
	 */
	public Page pageBySQL2(String countSql,String sql,int pageNo,int pageSize);
}
