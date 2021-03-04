package com.techelevator.tenmo.model;

public class Transfer {
	private Long transfer_id;
	private int transfer_type_id;
	private int transfer_status_id;
	private int account_from;
	private int account_to;
	private double amount;
	private String transfer_status;
	private String transfer_type;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Long getTransfer_id() {
		return transfer_id;
	}
	public void setTransfer_id(Long transfer_id) {
		this.transfer_id = transfer_id;
	}
	public int getTransfer_type_id() {
		return transfer_type_id;
	}
	public void setTransfer_type_id(int transfer_type_id) {
		this.transfer_type_id = transfer_type_id;
	}
	public int getTransfer_status_id() {
		return transfer_status_id;
	}
	public void setTransfer_status_id(int transfer_status_id) {
		this.transfer_status_id = transfer_status_id;
	}
	public int getAccount_from() {
		return account_from;
	}
	public void setAccount_from(int account_from) {
		this.account_from = account_from;
	}
	public int getAccount_to() {
		return account_to;
	}
	public void setAccount_to(int account_to) {
		this.account_to = account_to;	
	}
	
	public String getTransfer_status() {
		return transfer_status;
	}
	public void setTransfer_status(String transfer_status) {
		this.transfer_status = transfer_status;
	}
	public String getTransfer_type() {
		return transfer_type;
	}
	public void setTransfer_type(String transfer_type) {
		this.transfer_type = transfer_type;
	}
	
	@Override
	public String toString() {
		return " Transfers: " + transfer_id + " Transfer Type Id:" + transfer_type_id
				+ " Transfer Status Id: " + transfer_status_id + " Account From: " + account_from + " Account to: "
				+ account_to + " Amount: "+ amount;
	}
	
	
	
	
	
}
