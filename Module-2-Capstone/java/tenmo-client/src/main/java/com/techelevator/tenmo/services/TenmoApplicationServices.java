package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;

/*******************************************************************************************************
 * This is where you code Application Services required by your solution
 * 
 * Remember:  theApp ==> ApplicationServices  ==>  Controller  ==>  DAO
********************************************************************************************************/

public class TenmoApplicationServices {
	  public static String AUTH_TOKEN = "";
	  private final String BASE_URL;
	  private final RestTemplate restTemplate = new RestTemplate();
	  
	  public TenmoApplicationServices(String url) {
		  BASE_URL = url;
	  }
	  
	  public double getBalance(Long user_id) {
		 //Account newAccount = new Account();
		 double newAccountBalance = restTemplate.getForObject(BASE_URL + "/accounts/" + user_id, double.class);
		
		 return newAccountBalance; 
	  }
	  
	  public void sendBucks(int account_from, int account_to, double amount) {
		  Transfer newTransfer = new Transfer();
		  	newTransfer.setTransfer_type_id(2);
			newTransfer.setTransfer_status_id(2);
			newTransfer.setAccount_from(account_from);
			newTransfer.setAccount_to(account_to);
			newTransfer.setAmount(amount);
		  HttpHeaders headers = new HttpHeaders();
	      headers.setContentType(MediaType.APPLICATION_JSON);
	      HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);
	      System.out.println(entity);
	  }
}
