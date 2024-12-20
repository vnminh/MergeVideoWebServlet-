package model.bean;

import java.util.ArrayList;
import java.util.List;

public class Account {
	private String userID, username, password;
	private List<Integer> listPID = new ArrayList<Integer>();
	public Account(String userID, String username) {
		this.userID = userID;
		this.username = username;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Integer> getListPID() {
		return listPID;
	}
	public void setListPID(List<Integer> listPID) {
		this.listPID = listPID;
	}
	
}
