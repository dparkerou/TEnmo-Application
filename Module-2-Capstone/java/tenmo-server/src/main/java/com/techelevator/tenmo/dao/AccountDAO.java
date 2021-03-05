package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

	//Create 
	public void createAccount(Account newAccount);
	
	//Read 
	public double getBalanceByUserId(Long user_id);
	
	public List <Account> listAccounts();
	
	//Update 
	public void updateAccount(Account updatedAccount);
	
	//Delete 
	public void deleteAccount(int account_id);
	
}
