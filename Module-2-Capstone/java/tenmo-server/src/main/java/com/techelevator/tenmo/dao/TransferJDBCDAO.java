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
public class TransferJDBCDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	private AccountDAO accountDAO;
	
	public TransferJDBCDAO(DataSource dataSource, AccountDAO accountDAO) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.accountDAO = accountDAO;
	}
	
	@Override
	public void sendTransfer(int account_from, int account_to, double amount) {
		Transfer newTransfer = new Transfer();
		String sqlNewTransfer ="INSERT INTO transfers "
							  + "(transfer_id, transfer_type_id, transfer_status_id, "
							  + "account_from, account_to, amount) "	
							  +	"VALUES(?, ?, ?, ?, ?, ?) ";
		//do the same thing with request type 1 for request
		newTransfer.setTransfer_id(getNextTransferId());
		newTransfer.setTransfer_type_id(2);
		newTransfer.setTransfer_status_id(2);
		newTransfer.setAccount_from(account_from);
		newTransfer.setAccount_to(account_to);
		newTransfer.setAmount(amount);
		
		jdbcTemplate.update(sqlNewTransfer, newTransfer.getTransfer_id(), newTransfer.getTransfer_type_id(), newTransfer.getTransfer_status_id(),
								newTransfer.getAccount_from(), newTransfer.getAccount_to(), newTransfer.getAmount());
		//link to accountDAO to add and subtract 
	
		accountDAO.addToBalance(amount, account_to);
		accountDAO.subtractFromBalance(amount, account_from);
		
	}
	
	@Override
	public List<Transfer> listTransfersByAccount(int account_from) {
		List<Transfer> returnList = new ArrayList<Transfer>();

		
		String sqlListTransfers = "SELECT * " 
								+ "FROM transfers "
								+ "INNER JOIN transfer_statuses "
								+ "ON transfer_statuses.transfer_status_id = transfers.transfer_status_id "
								+ "INNER JOIN transfer_types "
								+ "ON transfer_types.transfer_type_id = transfers.transfer_type_id "
								+ "WHERE account_from = ? or account_to = ? ";
		
		
		SqlRowSet transferQuery = jdbcTemplate.queryForRowSet(sqlListTransfers, account_from, account_from);
		
		while(transferQuery.next()) {
			Transfer theTransfer =  mapRowToTransfer(transferQuery);
			returnList.add(theTransfer);
		}
		
		return returnList;
	}

	@Override
	public List<Transfer> listTransfersById(Long transfer_id) {
		List<Transfer> returnList = new ArrayList<Transfer>();
		
		String sqlListTransfers = "SELECT * " 
								+ "FROM transfers "
								+ "INNER JOIN transfer_statuses "
								+ "ON transfer_statuses.transfer_status_id = transfers.transfer_status_id "
								+ "INNER JOIN transfer_types "
							 	+ "ON transfer_types.transfer_type_id = transfers.transfer_type_id "
								+ "WHERE transfers.transfer_id = ? ";
		
		SqlRowSet transferQuery = jdbcTemplate.queryForRowSet(sqlListTransfers, transfer_id);
		
		while(transferQuery.next()) {
			Transfer theTransfer =  mapRowToTransfer(transferQuery);
			returnList.add(theTransfer);
		}
		
		return returnList;
	}

	@Override
	public void updateTransferStatus(Long transfer_id, int transfer_status_id) {
		String sqlUpdateTransfer = "UPDATE transfers "
								+ "SET transfer_status_id = ? "
								+ "WHERE transfer_id = ? ";	
		
		jdbcTemplate.update(sqlUpdateTransfer, transfer_status_id, transfer_id);

	}
	
	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer theTransfer = new Transfer();
	
		theTransfer.setTransfer_id(results.getLong("transfer_id")); 
		
		theTransfer.setTransfer_type_id(results.getInt("transfer_type_id"));
		
		theTransfer.setTransfer_status_id(results.getInt("transfer_status_id"));
		
		theTransfer.setAccount_from(results.getInt("account_from"));
		
		theTransfer.setAccount_to(results.getInt("account_to"));
		
		theTransfer.setAmount(results.getDouble("amount"));
		
		theTransfer.setTransfer_type(results.getString("transfer_type_desc"));
		
		theTransfer.setTransfer_status(results.getString("transfer_status_desc"));
		
		return theTransfer;
	}
			
	private long getNextTransferId() {
		SqlRowSet nextTransferIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
		
		if(nextTransferIdResult.next()) {
			return nextTransferIdResult.getLong(1);
		}else {
			throw new RuntimeException ("Something went wrong while getting an id for the new transfer");
		}
	}

}
