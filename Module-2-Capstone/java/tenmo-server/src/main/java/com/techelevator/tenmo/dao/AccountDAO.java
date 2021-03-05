package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

	//Create 
	public void createAccount(Account newAccount);
	
	//Read 
	public double getBalanceByUserId(Long user_id);
	
	public List <Account> listAccounts();
	
	public Account getAccountById(int id);
	
	//Update 
	public double addToBalance(double amount_to_add, int account_id);
	
	public double subtractFromBalance(double amount_to_sub, int account_id);
	
	//Delete 
	public void deleteAccount(int account_id);
	
}
