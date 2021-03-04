package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
		
		//Create
		public void createTransfer(Transfer newTransfer);
	
		//Read
		public List <Transfer> listTransfersByAccount(int account_from);
		
		public List <Transfer> listTransfersById(Long transfer_id);
		
		//Update 
		public void updateTransferStatus(Long transfer_id, int transfer_status_id);
}