package com.jyd.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
@SuppressWarnings("serial")
public class BaseEntity implements Serializable{
	@Id
	@Column(name = "SID", length = 32)
//	@GeneratedValue(generator = "uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid")
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="JYDSEQUENCES")
	@SequenceGenerator(name="JYDSEQUENCES",sequenceName="JYDSEQUENCES")
//	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
	@Column(name="DEL_FLAG",nullable=false)
	public boolean delFlag=false;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
			this.id=id;
	}

	public boolean isDelFlag() {
		return delFlag;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
