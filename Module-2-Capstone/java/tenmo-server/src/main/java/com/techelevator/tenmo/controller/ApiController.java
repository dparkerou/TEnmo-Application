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
import com.techelevator.tenmo.model.Users;

/*******************************************************************************************************
 * This is where you code any API controllers you may create
********************************************************************************************************/
//@RestController -- do we need this? this seemed to keep the server from starting this morning
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")

public class ApiController {
	private AccountDAO accountDAO;
	private TransferDAO transferDAO;
	private UserDAO userDAO;
	private JdbcTemplate jdbcTemplate;
	
	public ApiController(AccountDAO accountDAO, TransferDAO transferDAO, UserDAO userDAO ) {
		this.accountDAO = accountDAO;
		this.transferDAO = transferDAO;
		this.userDAO = userDAO;
		
	}

	@RequestMapping(path = " ", method = RequestMethod.GET)
    public List<User> list() {
		
        return userDAO.findAll();
    }
	
	@RequestMapping( path = " ", method = RequestMethod.GET)
	public User findUserByUsername(@RequestParam String username) {
		
	    return userDAO.findByUsername(username);
	}
	
	@RequestMapping( path = " ", method = RequestMethod.GET)
	public int findIdByUsername(@RequestParam String username) {
		
	   return userDAO.findIdByUsername(username);
	}
	
	@RequestMapping( path = "/accounts/{id} ", method = RequestMethod.GET)
	public double getBalanaceByUserId(@PathVariable Principal userInfo) {
		long user_id = userDAO.findIdByUsername(userInfo.getName());
		return accountDAO.getBalanceByUserId((int) user_id);
	}
	
	@RequestMapping( path = "/accounts ", method = RequestMethod.POST) 
	@ResponseStatus(value = HttpStatus.CREATED)
	public void updateAccountBalance(@RequestBody Account updatedAccount) {
		accountDAO.updateAccount(updatedAccount);
	}
	
	@RequestMapping ( path = "/transfers ", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createTransfer(@RequestBody Transfer newTransfer) {
		transferDAO.createTransfer(newTransfer);
	}
	
	// Possibly could refactor to combine the next two methods
	@RequestMapping (path = "/transfers ", method = RequestMethod.GET) 
	public List<Transfer> listTransfersByAccount(@RequestParam int account_from) {
		return transferDAO.listTransfersByAccount(account_from);
	}
	
	@RequestMapping (path = "/transfers/{id} ", method = RequestMethod.GET) 
	public List<Transfer> listTransfersById(@PathVariable Long transfer_id) {
		return transferDAO.listTransfersById(transfer_id);
	}
	
	@RequestMapping (path = "/transfers/{id} ", method = RequestMethod.POST)
	public void updateTransferStatus (@PathVariable Long transfer_id, @RequestParam int transfer_status_id) {
		transferDAO.updateTransferStatus(transfer_id, transfer_status_id);
	}
	
	
	
		 
}

	
	
	
	
	