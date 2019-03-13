package com.moment.info;

import java.io.Serializable;

public class PostInfo implements Serializable{
	private String pid;
	private String uid;
	private String uname;
	private String uhead;
	private String uage;
	private String ptime;
	private String pbeginTime;
	private String pendTime;
	private String ptravalAddress;
	private String pstartAddress;
	private String psaying;
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUhead() {
		return uhead;
	}

	public void setUhead(String uhead) {
		this.uhead = uhead;
	}

	public String getUage() {
		return uage;
	}

	public void setUage(String uage) {
		this.uage = uage;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	public String getPbeginTime() {
		return pbeginTime;
	}

	public void setPbeginTime(String pbeginTime) {
		this.pbeginTime = pbeginTime;
	}

	public String getPendTime() {
		return pendTime;
	}

	public void setPendTime(String pendTime) {
		this.pendTime = pendTime;
	}

	public String getPtravalAddress() {
		return ptravalAddress;
	}

	public void setPtravalAddress(String ptravalAddress) {
		this.ptravalAddress = ptravalAddress;
	}

	public String getPstartAddress() {
		return pstartAddress;
	}

	public void setPstartAddress(String pstartAddress) {
		this.pstartAddress = pstartAddress;
	}

	public String getPsaying() {
		return psaying;
	}

	public void setPsaying(String psaying) {
		this.psaying = psaying;
	}
	
	

}
