package com.techelevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.AccountJDBCDAO;

class AccountTest {
	
	private static SingleConnectionDataSource dataSource;
	
	private static AccountJDBCDAO accountDAO;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		dataSource = new SingleConnectionDataSource();	
		
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");							
		dataSource.setPassword("postgres1");						
		
		dataSource.setAutoCommit(false); 
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@BeforeEach
	void setUp() throws Exception {
		//Instantiate a new JDBCTemplate object to use in tests
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		accountDAO = new AccountJDBCDAO(dataSource);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
