package com.springboot.body;

/*
 * This class is a ResponseBody for Skills Assessment's Assignment Entity
 * */
public class AssignmentSA {
	
	int[] peers;
	int[] supervisors;
	int user;
	
	public int[] getPeers() {
		return peers;
	}
	public void setPeers(int[] peers) {
		this.peers = peers;
	}
	public int[] getSupervisors() {
		return supervisors;
	}
	public void setSupervisors(int[] supervisors) {
		this.supervisors = supervisors;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}	
}
