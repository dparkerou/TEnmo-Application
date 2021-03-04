package com.techelevator.tenmo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	public ApiController() {//is this correct??
		this.accountDAO = new AccountJDBCDAO(jdbcTemplate);
		this.transferDAO = new TransferJDBCDAO(jdbcTemplate);
		this.userDAO = new UserSqlDAO(jdbcTemplate);
		
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
	
	@RequestMapping( path = " ", method = RequestMethod.POST) 
	@ResponseStatus(value = HttpStatus.CREATED)
	public boolean create(User user) {
		return userDAO.create(user.getUsername(), user.getPassword());
	}
}
