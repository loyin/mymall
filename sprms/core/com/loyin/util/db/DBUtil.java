package com.jyd.util.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.jyd.util.Page;

public class DBUtil<T extends Serializable>
{
	private static final Logger log = Logger.getLogger(DBUtil.class);
	/**0:mysql 1:oracle 2:db2*/
	private static int dataBaseType;
	/**数据源*/
	@Resource
	private DataSource dataSource;
	static{
		String temp=ResourceBundle.getBundle("jdbc").getString("jdbc.driverClassName");
		if(temp!=null&&!"".equals(temp.trim())){
			if(temp.contains("mysql")){
				dataBaseType=0;
			}else if(temp.contains("oracle")){
				dataBaseType=1;
			}else if(temp.contains("db2")){
				dataBaseType=2;
			}
		}
	}
	public DBUtil(){
		log.debug("DBUtil已经创建！");
	}
	/**
	 * 执行 增 删 改
	 * 
	 * @param sql
	 * @param param SQL中传递参数
	 * @return
	 */
	public int executeSQL(StringBuffer sql, List<Object> param) throws Exception {
		 PreparedStatement ps=null;
		Connection conn=null;
		try{
			conn=this.getConnaction();
			ps = conn.prepareStatement(sql.toString());
			if (param != null && !param.isEmpty()) {
				int i = 1;
				for (Object obj : param)
					ps.setObject(i++, obj);
			}
			log.info(sql);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally{
			if(ps!=null)
			ps.close();
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
	}
	/**
	 * 执行 增 删 改
	 * 
	 * @param sql
	 * @param param
	 *            SQL中传递参数
	 * @return
	 */
	public int executeSQLBatch(StringBuffer sql, List<List<Object>> param) throws Exception {
		PreparedStatement ps=null;
		Connection conn=null;
		try{
			conn =this.getConnaction();
			ps = conn.prepareStatement(sql.toString());
			if (param != null && !param.isEmpty()) {
				int coun=0;
				for(List<Object> p:param){
					int i = 1;
					for (Object obj : p)
					ps.setObject(i++, obj);
					ps.addBatch();
					coun++;
					if(coun%100==0){
						ps.executeBatch();
					}
				}
			}
			log.info(sql);
			return ps.executeBatch().length;
		} catch (SQLException e) {
			log.error(e);
			throw e;
		}finally{
			if(ps!=null)
			ps.close();
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
	}
	/**
	 * 执行查询
	 * 
	 * @param sql
	 * @param param
	 * @return ResultSet
	 * @throws Exception
	 */
	public ResultSet executeQuery(String sql, List<Object> param,PreparedStatement ps,Connection conn)
			throws Exception {
		conn =this.getConnaction();
		ResultSet rs = null;
		ps = conn.prepareStatement(sql);
			if (param != null && !param.isEmpty()) {
				int i = 1;
				for (Object obj : param)
					ps.setObject(i++, obj);
			}
			log.info(sql);
			rs = ps.executeQuery();
		return rs;
	}
	
	/**
	 * 执行查询
	 * 
	 * @param sql
	 * @param param
	 *            参数
	 * @param List
	 *            &lt;String&gt;...clunmList 列名数组
	 * @return List&lt;Object[]&gt;
	 * @throws Exception
	 */
	public List<Object[]> query(StringBuffer sql, List<Object> param,
			List<String>... clunmList) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps=null;
		Connection conn=null;
		try {
			rs = executeQuery(sql.toString(), param,ps,conn);
			if (rs != null) {
				List<Object[]> list = new ArrayList<Object[]>();
				// 获取结果集元数据
				ResultSetMetaData rsmd = rs.getMetaData();
				// 总列数
				int size = rsmd.getColumnCount();
				if (clunmList != null) {
					for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
						clunmList[0].add(rsmd.getColumnLabel(cluIndex));
					}
				}
				while (rs.next()) {
					Object[] value = new Object[size];
					for (int j = 1; j <= size; j++) {
						value[j - 1] = rs.getObject(j);
					}
					list.add(value);
				}
				return list;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
					if(ps!=null)
					ps.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
		return null;
	}
	/**
	 * 执行查询
	 * 
	 * @param sql
	 * @param param
	 *            参数
	 * @param List  &lt;String&gt;...clunmList 列名数组
	 * @param tempEntity 外部传递的空实例。
	 * @return List&lt;T&gt;
	 * @throws Exception
	 */
	public List<T> queryEntityList(StringBuffer sql, List<Object> param,T tempEntity, List<String>... clunmList) throws Exception
	{
		ResultSet rs = null;
		PreparedStatement ps=null;
		Connection conn=null;
		try {
			rs = executeQuery(sql.toString(), param,ps,conn);
			if (rs != null) {
				List<T> list = new ArrayList<T>();
				// 获取结果集元数据
				ResultSetMetaData rsmd = rs.getMetaData();
				// 总列数
				int size = rsmd.getColumnCount();
				if (clunmList != null&&clunmList.length>0) {
					for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
						clunmList[0].add(rsmd.getColumnLabel(cluIndex));
					}
				}
				while (rs.next()) {
					Object obj=BeanUtils.cloneBean(tempEntity);
					for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
						String colname=rsmd.getColumnName(cluIndex);
						if(colname!=null&&!"".equals(colname))
						BeanUtils.copyProperty(obj, colname.toLowerCase(), rs.getObject(cluIndex));
					}
					list.add((T)obj);
				}
				return list;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
					if(ps!=null)
					ps.close();
				} catch (SQLException e1) {
					throw e1;
				}
			}
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
		return null;
	}
	/**
	 * 执行单一对象查询
	 * 
	 * @param sql
	 * @param param
	 *            参数
	 * @param po 需要赋值的对象
	 * @throws Exception
	 */
	public Object queryEntity(StringBuffer sql, List<Object> param,Object po) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps=null;
		Connection conn=null;
		try {
			if(dataBaseType==0){
				sql.append(" limit 0,1");
			}
			rs = executeQuery(sql.toString(), param,ps,conn);
			if (rs != null) {
				// 获取结果集元数据
				ResultSetMetaData rsmd = rs.getMetaData();
				// 总列数
				int size = rsmd.getColumnCount();
					
				if (rs.next()) {
					for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
						String colname=rsmd.getColumnName(cluIndex);
						if(colname!=null&&!"".equals(colname))
						BeanUtils.copyProperty(po, colname.toLowerCase(), rs.getObject(cluIndex));
					}
				}else{
					return null;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					rs.close();
					if(ps!=null)
					ps.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
		return po;
	}
	public Object queryByExcample(Object po) throws Exception{

		StringBuffer sql = new StringBuffer("SELECT * FROM ");
		List<Object> params = new ArrayList<Object>();
		Table table = po.getClass().getAnnotation(Table.class);
		if (table != null)
			sql.append(table.name());
		else
			sql.append(po.getClass().getSimpleName().toUpperCase());
		sql.append(" WHERE ");
		Field[] fields = po.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			String fieldname = field.getName();
			if (!"serialVersionUID".equals(fieldname))
			{
				Object fieldvalue = PropertyUtils.getProperty(po, fieldname);
				if (fieldvalue != null)
				{
					Disabled disabled=field.getAnnotation(Disabled.class);
					if(disabled!=null){
						continue;
					}
					Column column = field.getAnnotation(Column.class);
					if (column != null)
					{
						sql.append(column.name());
						sql.append("=? AND ");
						params.add(fieldvalue);
					} else
					{
						sql.append(fieldname);
						sql.append(" =? AND ");
						params.add(fieldvalue);
					}
				}
			}
		}
		sql.append("END");
		return this.queryEntity(new StringBuffer(sql.toString().replaceAll("AND END", "").replaceAll("END", "")),params,po);
	}
	public boolean saveOrUpdate(Object po) throws Exception{
		Field[] fields = po.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			if (!"serialVersionUID".equals(field.getName())){
				Id id=field.getAnnotation(Id.class);
				if(id!=null){
					Object fieldvalue = PropertyUtils.getProperty(po, field.getName());
					if(fieldvalue!=null){
						this.update(po);
						return true;
					}
				}
			}
		}
		this.insert(po);
		return true;
	}
	public void insert(Object po) throws Exception
	{
		StringBuffer sql = new StringBuffer("INSERT INTO ");
		StringBuffer valuesql = new StringBuffer(" VALUES(");
		List<Object> params = new ArrayList<Object>();
		Table table = po.getClass().getAnnotation(Table.class);
		if (table != null)
			sql.append(table.name());
		else
			sql.append(po.getClass().getSimpleName().toUpperCase());
		sql.append("(");
		Field[] fields = po.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			String fieldname = field.getName();
			if (!"serialVersionUID".equals(fieldname))
			{
				if(dataBaseType==1){//如果是ORACLE数据库
					Id id=field.getAnnotation(Id.class);
					SequenceGenerator sqg=field.getAnnotation(SequenceGenerator.class);
					if(id!=null&&sqg!=null){
						Column column = field.getAnnotation(Column.class);
						if (column != null)
						{
							sql.append(column.name());
						}else{
							sql.append(fieldname);
						}
						sql.append(",");
						valuesql.append(sqg.sequenceName()+".NextVal,");
						continue;
					}
				}//---结束判断oracle数据库
				
				Object fieldvalue = PropertyUtils.getProperty(po, fieldname);
				if (fieldvalue != null)
				{	Disabled disabled=field.getAnnotation(Disabled.class);
					if(disabled!=null){
						continue;
					}
					Column column = field.getAnnotation(Column.class);
					if (column != null)
					{
						sql.append(column.name());
						sql.append(",");
						valuesql.append("?,");
						params.add(fieldvalue);
					} else
					{
						sql.append(fieldname);
						sql.append(",");
						valuesql.append("?,");
						params.add(fieldvalue);
					}
				}
			}
		}
		sql.append(")");
		valuesql.append(")");
		sql.append(valuesql);
		this.executeSQL(new StringBuffer(sql.toString().replaceAll(",\\)", ")")), params);
	}
	public void update(Object po) throws Exception
	{
		StringBuffer sql = new StringBuffer("UPDATE ");
		StringBuffer wheresql=new StringBuffer(" WHERE ");
		List<Object> params = new ArrayList<Object>();
		Table table = po.getClass().getAnnotation(Table.class);
		if (table != null)
			sql.append(table.name());
		 else
			sql.append(po.getClass().getSimpleName().toUpperCase());
		sql.append(" SET ");
		
		Field[] fields = po.getClass().getDeclaredFields();
		Object keyvalue=null;
		for (Field field : fields)
		{
			String fieldname = field.getName();
			Id id=field.getAnnotation(Id.class);
			if(id!=null){
				Object fieldvalue = PropertyUtils.getProperty(po, fieldname);
				Column column = field.getAnnotation(Column.class);
				if (column != null)
				{
					wheresql.append(column.name());
				}else{
					wheresql.append(fieldname);
				}
				    wheresql.append(" =?");
				    keyvalue=fieldvalue;
				continue;
			}
			if (!"serialVersionUID".equals(fieldname))
			{
				Object fieldvalue = PropertyUtils.getProperty(po, fieldname);
				if (fieldvalue != null)
				{
					Disabled disabled=field.getAnnotation(Disabled.class);
					if(disabled!=null){
						continue;
					}
					Column column = field.getAnnotation(Column.class);
					if (column != null)
					{
						sql.append(column.name());
						sql.append(" =?,");
					} else
					{
							sql.append(fieldname);
							sql.append(" =?,");
					}
					params.add(fieldvalue);
				}
			}
		}
		params.add(keyvalue);
		sql.append("EE");
		sql.append(wheresql);
		this.executeSQL(new StringBuffer(sql.toString().replaceAll(",EE", "")), params);
	}
	public void delete(Object po) throws Exception
	{
		StringBuffer sql = new StringBuffer("DELETE FROM ");
		StringBuffer wheresql=new StringBuffer(" WHERE ");
		List<Object> params = new ArrayList<Object>();
		Table table = po.getClass().getAnnotation(Table.class);
		if (table != null)
			sql.append(table.name());
		 else
			sql.append(po.getClass().getSimpleName().toUpperCase());
				
		Field[] fields = po.getClass().getDeclaredFields();
		Object keyvalue=null;
		for (Field field : fields)
		{
			String fieldname = field.getName();
			Id id=field.getAnnotation(Id.class);
			if(id!=null){
				Object fieldvalue = PropertyUtils.getProperty(po, fieldname);
				Column column = field.getAnnotation(Column.class);
				if (column != null)
				{
					wheresql.append(column.name());
				}else{
					wheresql.append(fieldname);
				}
				    wheresql.append(" =? AND ");
				    keyvalue=fieldvalue;
				continue;
			}
			if (!"serialVersionUID".equals(fieldname))
			{
				Object fieldvalue = PropertyUtils.getProperty(po, fieldname);
				if (fieldvalue != null)
				{
					Disabled disabled=field.getAnnotation(Disabled.class);
					if(disabled!=null){
						continue;
					}
					params.add(fieldvalue);
				}
			}
		}
		params.add(keyvalue);
		sql.append(wheresql);
		sql.append("EE");
		this.executeSQL(new StringBuffer(sql.toString().replaceAll("AND EE", "").replaceAll("EE", "")), params);
	}
	/***
	 * 分页sql查询 （暂时支持mysql）
	 * 
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @param param
	 * @param clunmList
	 * @return
	 * @throws Exception
	 */
	public Page pageQuery(StringBuffer sql, int pageNo, int pageSize,
			List<Object> param, List<String>... clunmList) throws Exception {
		ResultSet rs = null;
		ResultSet rscount = null;
		List<Object[]> list = null;
		PreparedStatement ps=null;
		Connection conn=null;
		try {
			if (pageNo == 0) {
				pageNo = 1;
			}
			StringBuffer countSql = new StringBuffer("select count(*) ");
			countSql.append(sql.substring(sql.indexOf(" from ")));
			int count = 0;
			if(countSql.toString().indexOf(" order ")>0){
				rscount = executeQuery(countSql.toString().substring(0,countSql.toString().indexOf(" order ")), param,ps,conn);
			}else
			if(countSql.toString().indexOf(" group ")>0){
				rscount = executeQuery(countSql.toString().substring(0,countSql.toString().indexOf(" group ")), param,ps,conn);
			}else{
				rscount = executeQuery(countSql.toString(), param,ps,conn);
			}
			rscount.next();
			count = rscount.getInt(1);
			if (count <= 0) {
				return new Page(0, pageNo, pageSize, null);
			}
			if (param == null) {
				param = new ArrayList<Object>();
			}
			switch (dataBaseType){
			case 0://mysql
				sql.append(" limit ?,?");
				param.add((pageNo - 1) * pageSize);
				param.add(pageSize);
				break;
			case 1://oracle
				StringBuffer tempstr=new StringBuffer(sql);
				sql=new StringBuffer("SELECT * FROM (SELECT AOALCS.*, ROWNUM RN FROM (");
				sql.append(tempstr);
				sql.append(")AOALCS WHERE ROWNUM <= ?)WHERE RN > ?");
				param.add(pageNo * pageSize);
				param.add((pageNo - 1) * pageSize);
				break;
			case 2://db2
				
				break;
			}
			
			rs = executeQuery(sql.toString(), param,ps,conn);
			if (rs != null) {
				list = new ArrayList<Object[]>();
				// 获取结果集元数据
				ResultSetMetaData rsmd = rs.getMetaData();
				// 总列数
				int size = rsmd.getColumnCount();
				if (clunmList != null) {
					for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
						clunmList[0].add(rsmd.getColumnLabel(cluIndex));
					}
				}
				while (rs.next()) {
					Object[] value = new Object[size];
					for (int j = 1; j <= size; j++) {
						value[j - 1] = rs.getObject(j);
					}
					list.add(value);
				}
			}
			return new Page(count, pageNo, pageSize, list);
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null) {
				try {
					if (rscount != null)
						rscount.close();
					rs.close();
					if(ps!=null)
					ps.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
	}
	public Connection getConnaction() throws Exception{
		return DataSourceUtils.doGetConnection(dataSource);//.getConnection(dataSource);
	}
	/**
	 * 查询所有表名
	 * @param catalog
	 * @param schemaPattern
	 * @param tableNamePattern
	 * @param types
	 * @return {catalog,schema,tableName,types}
	 * @throws Exception
	 */
	public List<String[]> getAllTables(String catalog,String schemaPattern,String tableNamePattern,String[] types, List<String>... clunmList)throws Exception{
		List<String[]> tables=new ArrayList<String[]>();
		DatabaseMetaData md=null;
		ResultSet rs = null;
		Connection conn=this.getConnaction();
		try{
		md=conn.getMetaData();
		rs=md.getTables(catalog,schemaPattern,tableNamePattern,types);
		// 获取结果集元数据
		ResultSetMetaData rsmd = rs.getMetaData();
		// 总列数
		int size = rsmd.getColumnCount();
		if (clunmList != null) {
			for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
				clunmList[0].add(rsmd.getColumnLabel(cluIndex));
			}
		}
		while(rs.next()){
			String[] tb=new String[size];
			for(int i=1;i<=size;i++){
				tb[i-1]=rs.getString(i);
			}
			tables.add(tb);
		}
		}catch(Exception e){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
		return tables;
	}
	/**
	 * 
	 * @param catalog
	 * @param schemaPattern
	 * @param tableNamePattern
	 * @param columnNamePattern
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getColumns(String catalog,String schemaPattern,String tableNamePattern,String columnNamePattern, List<String>... clunmList)throws Exception{
		List<String[]> tables=new ArrayList<String[]>();
		DatabaseMetaData md=null;
		ResultSet rs = null;
		Connection conn=this.getConnaction();
		try{
		md=conn.getMetaData();
		rs=md.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
		// 获取结果集元数据
		ResultSetMetaData rsmd = rs.getMetaData();
		// 总列数
		int size = rsmd.getColumnCount();
		if (clunmList != null) {
			for (int cluIndex = 1; cluIndex <= size; cluIndex++) {
				clunmList[0].add(rsmd.getColumnLabel(cluIndex));
			}
		}
		while(rs.next()){
			String[] tb=new String[size];
			for(int i=1;i<=size;i++){
				tb[i-1]=rs.getString(i);
			}
			tables.add(tb);
		}
		}catch(Exception e){
			throw e;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(conn!=null){
				//DbUtils.close(conn);
			}
		}
		return tables;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/*public static void main(String[] args) throws Exception {
		ApplicationContext factory = new ClassPathXmlApplicationContext(new String[] {"spring-DataBase.xml"});
		DBUtil dbUtil= new DBUtil();
		DataSource dataSource =(DataSource) factory.getBean("dataSource");
		dbUtil.setDataSource(dataSource);
		List<String> columnList=new ArrayList<String>();
		List<String[]> list=dbUtil.getColumns(null,null, null,null,columnList);
		for(String col:columnList){
			System.out.print(col+"\t");
		}
		System.out.print("\n");
		for(String[] table:list){
			for(String t:table){
				System.out.print(t+"\t");
			}
			System.out.print("\n");
		}
	}*/

}
