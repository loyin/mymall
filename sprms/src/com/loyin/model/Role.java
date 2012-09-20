package com.jyd.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.jyd.po.BaseEntity;
/**
 * 角色
 * @author loyin
 */
@Entity
@Table(name="SYS_ROLE")
public class Role extends BaseEntity{
	private static final long serialVersionUID = 8704127454343678568L;
	/**角色*/
	@Column(name="NAME",length=100,unique=true)
	private String name;
	/**备注*/
	@Column(name="REMARK",length=200,nullable=true)
	private String remark;
	/**角色菜单*/
	@ManyToMany(targetEntity=Menu.class, cascade = { CascadeType.PERSIST,CascadeType.MERGE },fetch=FetchType.LAZY)
	@JoinTable(name="SYS_ROLE_MENU",joinColumns=@JoinColumn(name = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "MENU_ID"))
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Menu> menus=new HashSet<Menu>(0);
	public Role(){}
	public Role(Long id){
		this.id=id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Set<Menu> getMenus() {
		return menus;
	}
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
}
