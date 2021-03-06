package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.AccountJDBCDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferJDBCDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;


/*******************************************************************************************************
 * This is where you code any API controllers you may create
********************************************************************************************************/
@RestController 
//@RequestMapping("//localhost:8080")
@PreAuthorize("isAuthenticated()")

public class ApiController {
	private AccountDAO accountDAO;
	private TransferDAO transferDAO;
	private UserDAO userDAO;
	
	public ApiController(AccountDAO accountDAO, TransferDAO transferDAO, UserDAO userDAO ) {
		this.accountDAO = accountDAO;
		this.transferDAO = transferDAO;
		this.userDAO = userDAO;
		
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> list() {
        return userDAO.findAll();
        
    }
	
	@RequestMapping( path = "/users/", method = RequestMethod.GET)
	public User findUserByUsername(@RequestParam String username) {
		
	    return userDAO.findByUsername(username);
	}
	
	@RequestMapping( path = "/accounts/balance", method = RequestMethod.GET)
	public double getBalanaceByUserId(Principal userInfo) {
		long userId = userDAO.findIdByUsername(userInfo.getName());
		return accountDAO.getBalanceByUserId(userId);  
	}
	
	@RequestMapping( path = "/accounts", method = RequestMethod.GET)
	public List<Account> getAccounts() {
		return accountDAO.listAccounts();
	}
	
	@RequestMapping ( path = "/transfers/send", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createTransfer(@RequestBody Transfer newTransfer, Principal userInfo) {
		int userId = userDAO.findIdByUsername(userInfo.getName());
		transferDAO.sendTransfer(userId, newTransfer.getAccount_to(), newTransfer.getAmount());
		
	}
	
	@RequestMapping ( path = "/accounts/transfers/{id}", method = RequestMethod.GET) 
	public List<Transfer> listTransfersByAccount(@PathVariable int id) {
		return transferDAO.listTransfersByAccount(id);
	}
	
	//@RequestMapping ( path = "/transfers/{id}", method = RequestMethod.GET) 
	//public List<Transfer> listTransfersById(@PathVariable int id) {
		//return transferDAO.listTransfersById((long) id);
	//}
	
		 
}

	
	
	
	
	