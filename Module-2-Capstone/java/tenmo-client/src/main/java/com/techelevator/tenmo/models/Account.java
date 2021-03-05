package com.techelevator.tenmo.models;

public class Account {
	private Long account_id;
	private int user_id;
	private double balance;
	public Long getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Accounts: " + account_id + " User Id: " + user_id + " Balance: " + balance;
	}

	
	
	
	
	
}
