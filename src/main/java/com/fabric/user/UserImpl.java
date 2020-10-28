package com.fabric.user;

import java.io.Serializable;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class UserImpl implements User,Serializable{
	
	private static final long serialVersionUID = 1L;
	protected String name;
	protected Set<String> roles;
	protected String account;
	protected String affiliation;
	protected Enrollment enrollment;
	protected String mspId;
	
	
	public UserImpl(String username, String orgname, String mspId, Enrollment adminEnrollment) {
		this.name=username;
		this.account=orgname;
		this.mspId=mspId;
		this.enrollment=adminEnrollment;
	}
	@Override
	public String toString() {
		return "UserImpl [name=" + name + ", roles=" + roles + ", account=" + account + ", affiliation=" + affiliation
				+ ", enrollment=" + enrollment + ", mspId=" + mspId + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public Enrollment getEnrollment() {
		return enrollment;
	}
	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}
	public String getMspId() {
		return mspId;
	}
	public void setMspId(String mspId) {
		this.mspId = mspId;
	}
	
}
