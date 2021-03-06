package com.techelevator.tenmo.services;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

/*******************************************************************************************************
 * This is where you code Application Services required by your solution
 * 
 * Remember:  theApp ==> ApplicationServices  ==>  Controller  ==>  DAO
********************************************************************************************************/

public class TenmoApplicationServices {
	
	  private final String BASE_URL = "http://localhost:8080";
	  private final RestTemplate restTemplate = new RestTemplate();
	  private final Console console = new Console();
	  
	  public TenmoApplicationServices() {
		  
	  }
	  
	  public double getBalance(String AUTH_TOKEN) {
		double newAccountBalance = 0.0;
		 try {
		    newAccountBalance = restTemplate.exchange(BASE_URL + "/accounts/balance", HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), double.class).getBody();
		    } catch (RestClientResponseException ex) {
		      System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
		    } catch (ResourceAccessException ex) {
		      System.out.print(ex.getMessage());
		    }
		 return newAccountBalance; 
	  }
	  
	  public void sendBucks(int account_from, int account_to, double amount, String AUTH_TOKEN) {
		  Transfer newTransfer = new Transfer();
			newTransfer.setAccount_to(c);
			newTransfer.setAmount(amount);
			try {
			restTemplate.postForObject(BASE_URL + "/transfers/send", makeTransferEntity(newTransfer, AUTH_TOKEN), Transfer.class);
			}catch (RestClientResponseException ex) {
			System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
			}
	  }
	  
	  public Transfer[] tranferList(String AUTH_TOKEN, int id) {

		  Transfer [] transfers = null;
		  try {
		  transfers = restTemplate.exchange(BASE_URL  + "/accounts/transfers/" + id, HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), Transfer[].class).getBody();
		  } catch (RestClientResponseException ex) {
		  System.out.print(ex.getRawStatusCode() + " : " + ex.getStatusText());
		  }
		  
		  for (Transfer transfer : transfers) {
			  Long trans_id = transfer.getTransfer_id();
			  String type = transfer.getTransfer_type();
			  int account_from = transfer.getAccount_from();
			  int account_to = transfer.getAccount_to();
			  double amount = transfer.getAmount();
			  String status = transfer.getTransfer_status();
			  System.out.println("ID: " + trans_id + " Type: " + type + " From: " + account_from + " To: " + account_to + " Amount: $"+ String.format("%.2f",amount) + " Status: "+ status);
		  }
		  return transfers;
	  }
	  
	  public User[] userList(String AUTH_TOKEN) {
		User[]users;
		users = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(AUTH_TOKEN), User[].class).getBody();
		for (User user : users) {
			int id = user.getId();
			String username = user.getUsername();
			System.out.println(id + " : " + username);
		}
		return users;
	  }
		 
	  private HttpEntity makeAuthEntity(String AUTH_TOKEN) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setBearerAuth(AUTH_TOKEN);
	        HttpEntity entity = new HttpEntity<>(headers);
	        return entity;
	    }
	  
	  private HttpEntity<Transfer> makeTransferEntity(Transfer newTransfer, String AUTH_TOKEN) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(AUTH_TOKEN);
	        HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);
	        return entity;
	    }

	

}
