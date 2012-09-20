package com.jyd.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jyd.po.BaseEntity;
import com.jyd.util.MD5;
/**
 * 系统账号用户
 * @author loyin
 */
@Entity
@Table(name="SYS_USER")
public class User extends BaseEntity {
	/**默认密码**/
	private static String defaultPwd="123456";
	private static final long serialVersionUID = 1L;
	/**所属企业*/
	@ManyToOne
	@JoinColumn(name="enterpriseid",nullable=true)
	private Enterprise enterprise;
	/**账号*/
	@Column(name="USER_NO",length=100,unique=true)
	private String userNo;
	/**姓名*/
	@Column(name="USER_NAME",length=100)
	private String name;
	/**密码 默认为123456*/
	@Column(name="PWD",length=100)
	private String password=null;
	/**注册日期*/
	@Column(name="REGDATE")
	private Date regDate;
	/**工作电话*/
	@Column(name="TEL",length=20)
	private String telephone;
	/**手机*/
	@Column(name="PHONE",length=20)
	private String phone;
	@Column(name="EMAIL",length=50)
	private String email;
	@ManyToOne
	@JoinColumn(name="ROLE_ID",nullable=true)
	private Role role;
	
    public User(){
    }
    public User(Long id) {
        this.id = id;
    }
	public String getPassword() {
		if(this.password==null||"".equals(this.password)){
				this.password=MD5.getMD5ofStr(defaultPwd);
		}
		return password;
	}
	public void setPassword(String password) {
		if(password==null||"".equals(password)){
				this.password=MD5.getMD5ofStr(defaultPwd);
		}else{
			this.password = password;
		}
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Enterprise getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	
}
