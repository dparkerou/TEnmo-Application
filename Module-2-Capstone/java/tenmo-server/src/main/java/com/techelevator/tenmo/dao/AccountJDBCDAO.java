package com.techelevator.tenmo.dao;

import java.util.ArrayList;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

@Component
//might want to add some defensive programming here 
public class AccountJDBCDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public AccountJDBCDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void createAccount(Account newAccount) {
		String sqlNewAccount ="INSERT INTO accounts "
				  			 + "(account_id, user_id, balance) "
				             + "VALUES(?,?, ?)";
		
		newAccount.setAccount_id(getNextAccountId());

		jdbcTemplate.update(sqlNewAccount, newAccount.getAccount_id(), newAccount.getUser_id(), newAccount.getBalance());

	}

	@Override
	public double getBalanceByUserId(Long user_id) {
		Account returnAccount = new Account();
		
		String sqlreturnAccount = "SELECT * " 
								+ "FROM accounts "
								+ "WHERE user_id = ? ";
		
		SqlRowSet accountQuery = jdbcTemplate.queryForRowSet(sqlreturnAccount, user_id);
		
		if(accountQuery.next()) {
			returnAccount =  mapRowToAccount(accountQuery);
		}
		
		return returnAccount.getBalance();
	}

	@Override
	public List<Account> listAccounts() {
		List<Account> returnList = new ArrayList<Account>();
		
		String sqlListAccounts = "SELECT * " 
							   + "FROM accounts";
								
		
		SqlRowSet accountQuery = jdbcTemplate.queryForRowSet(sqlListAccounts);
		
		while(accountQuery.next()) {
			Account theAccount =  mapRowToAccount(accountQuery);
			returnList.add(theAccount);
		}
		
		return returnList;
	}

	@Override
	public void updateAccount(Account updatedAccount) {
		String sqlUpdateAccount = "UPDATE accounts "
				                + "SET balance = ? WHERE account_id = ?";	
		
		if (updatedAccount.getBalance() > 0) {
		jdbcTemplate.update(sqlUpdateAccount, updatedAccount.getBalance(), updatedAccount.getAccount_id());
		} else {
			System.out.println("Insufficient Funds");
		}
		//we may want to change this to need a balance and an account

	}

	@Override
	public void deleteAccount(int account_id) {
		String sqlDeleteAccount = "DELETE from accounts where account_id = ? ";
		
		jdbcTemplate.update(sqlDeleteAccount, account_id);

	}
	
	private Account mapRowToAccount(SqlRowSet results) {
		Account theAccount = new Account();
		
		theAccount.setAccount_id(results.getLong("account_id"));
		theAccount.setUser_id(results.getInt("user_id"));
		theAccount.setBalance(results.getDouble("balance"));
		
		return theAccount;
	}
	
	private long getNextAccountId() {
		SqlRowSet nextAccountIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_account_id')");
		
		if(nextAccountIdResult.next()) {
			return nextAccountIdResult.getLong(1);
		}else {
			throw new RuntimeException ("Something went wrong while getting an id for the new transfer");
		}
	}

}
