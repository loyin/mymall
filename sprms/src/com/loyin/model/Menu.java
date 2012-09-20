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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.jyd.po.BaseEntity;
/***
 * 菜单
 * @author loyin
 */
@Entity
@Table(name="SYS_MENU")
public class Menu extends BaseEntity {
	private static final long serialVersionUID = -5055219605812547528L;
	public Menu(){}
	public Menu(Long id){this.id=id;}
	/**菜单名*/
	@Column(name="MENUNAME",nullable=false,length=200)
	private String name;
	/**链接*/
	@Column(name="URL",length=200)
	private String url;
	/**菜单类型 0:菜单1:按钮*/
	private Integer type;
	/**菜单等级*/
	@Column(name="MENU_LEVEL",nullable=true)
	private Integer menuLevel;
	/**父菜单*/
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_ID",nullable=true)
	private Menu parent;
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	private Set<Menu> childrens=new HashSet<Menu>(0);
	/**排列顺序*/
	@Column(name="ORDER_NUM")
	private Integer orderNum;
	/**菜单调用的js事件*/
	@Column(name="MENU_EVENT",length=50)
	private String event;
	/**菜单安全码*/
	@Column(name="SAFECODE",length=50)
	private String safeCode;
	/**图标*/
	@Column(name="ICON_URL",length=50)
	private String icon;
	/**角色菜单*/
	@ManyToMany(targetEntity=Role.class, cascade = { CascadeType.PERSIST,CascadeType.MERGE },fetch=FetchType.LAZY)
	@JoinTable(name="SYS_ROLE_MENU",joinColumns=@JoinColumn(name = "MENU_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Role> roles=new HashSet<Role>(0);

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Menu getParent() {
		return parent;
	}
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Menu> getChildrens() {
		return childrens;
	}
	public void setChildrens(Set<Menu> childrens) {
		this.childrens = childrens;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getSafeCode() {
		return safeCode;
	}
	public void setSafeCode(String safeCode) {
		this.safeCode = safeCode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
